package org.jemco.simplebpm.runtime.components;

import java.util.List;

import org.jemco.simplebpm.WorkflowService;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.StateTransition;
import org.jemco.simplebpm.process.SubProcessRole;
import org.jemco.simplebpm.runtime.SessionUtils;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.jemco.simplebpm.runtime.WorkflowSessionImpl;

public class SubNodeProcessor implements NodeProcessor {

	private ExecutionStateService executionStateService;
	
	private WorkflowService workflowService;
		
	public SubNodeProcessor(ExecutionStateService executionStateService, WorkflowService workflowService) {
		super();
		this.executionStateService = executionStateService;
		this.workflowService = workflowService;
	}
	
	public void handle(WorkflowSession session, String transition)  throws Exception {
		
		WorkflowSession subSession = createSubSession(session);
		// use to track if state of any sub-session moves to complete within this context
		boolean wasComplete = subSession != null ? subSession.isComplete() : false;
		processSession(subSession, transition, false);
		
		// was there an open sub process at the start of this session that has now completed ?
		if (subSession != null && (wasComplete == false && subSession.isComplete())) {
			
			// override blocking nature of sub-process parent state & set current transition to null
			session.getExecutionState().getCurrentToken().setBlocking(false);
			session.getContext().addContextVariable(WorkflowSessionImpl.TRANSITION_CONTEXT_KEY, null);
			
		}
		
	}
	
	private void addExecutionState(WorkflowSession session) {
		// check if we have an execution state tied to this sub-session, if not create.
		String id = getProcessId(SessionUtils.getCurrentNode(session), session);
		if (getExecutionState(session.getExecutionState().getChildren(), id) == null) {
			Process subProcess = getSubProcess(SessionUtils.getCurrentNode(session));
			ExecutionState subExecutionState = executionStateService.newExecutionContext(id, subProcess);
			session.getExecutionState().getChildren().add(subExecutionState);
		}
		
	}
	
	@Override
	public void handle(WorkflowSession session)  throws Exception {		
		
		// do we need to create an execution state for this sub-session?
		addExecutionState(session);
		
		final String transition = (String) session.getContext().getContextVar(WorkflowSessionImpl.TRANSITION_CONTEXT_KEY);
		
		// there is a case where on first invocation, the first transition provided will match the last exited node of the parent.
		// in this case we ignore the transition provided and explicitly set to null.
		State previous = session.getProcess().getState(session.getExecutionState().getPreviousToken().getName());
		StateTransition targetTransition = FunctionUtils.retrieveObjectFromCollection(previous.getExitTransitions(), new Predicate<StateTransition>(){
			public boolean accept(StateTransition object) {
				return object.getName().equals(transition);
			}});
		
		if (targetTransition != null) {
			// force transition to null.
			session.getContext().addContextVariable(WorkflowSessionImpl.TRANSITION_CONTEXT_KEY, null);
			handle(session, null);
			
		} else {
			handle(session, transition);
		}
		
		
		
		
	}
	
	private void processSession(WorkflowSession session, String transition, boolean override) throws Exception {
		if (session != null && !session.isComplete()) {
			if (null != transition) {
				session.execute(transition);
			} else {
				session.execute(override);
			}
		} else {
			// Log ?
		}
		
	}
	
	protected String getProcessId(State targetState, WorkflowSession session) {
		SubProcessRole subProcessRole = targetState.getRole(SubProcessRole.class);
		if(subProcessRole != null) {
			return subProcessRole.getSubProcess().getName() + ":" + session.getExecutionState().getId();
		}
		return null;
	}
	
	protected Process getSubProcess(State targetState) {
		SubProcessRole subProcessRole = targetState.getRole(SubProcessRole.class);
		return subProcessRole != null ? subProcessRole.getSubProcess() : null;
	}
	
	protected WorkflowSession createSubSession(WorkflowSession session) {
		State targetState = SessionUtils.getCurrentNode(session);
		Process subProcess = getSubProcess(targetState);
		if(subProcess != null) {
			String id = getProcessId(targetState, session);
			
			ExecutionState subState = getExecutionState(session.getExecutionState().getChildren(), id);
			
			// load new sub-session to cover the process node - it may complete to the end automatically so let this function complete
			WorkflowSession subSession = workflowService.newSession(subProcess, session.getContext(), subState);
			return subSession;
		}
		return null;
	}
	
	protected ExecutionState getExecutionState(List<ExecutionState> processStates, final String id) {
		List<ExecutionState> subProcessStates = FunctionUtils.predicateCollection(processStates, new Predicate<ExecutionState>() {
			@Override
			public boolean accept(ExecutionState state) {
				return state.getId().equals(id);
			}});
		return subProcessStates.size() == 1 ? subProcessStates.get(0) : null;
	}




}
