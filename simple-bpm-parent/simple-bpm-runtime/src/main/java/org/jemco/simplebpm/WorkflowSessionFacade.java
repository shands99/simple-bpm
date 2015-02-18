package org.jemco.simplebpm;

import java.util.List;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.SubProcessRole;
import org.jemco.simplebpm.runtime.execution.Context;
import org.jemco.simplebpm.runtime.execution.ExecutionState;

public class WorkflowSessionFacade implements WorkflowSession {

	private final ExecutionState executionState;
	
	private final ActionExecutor actionExecutor;
	
	private final Context context;
	
	private final EventService eventService;
	
	private WorkflowSession delegate;
	
	public WorkflowSessionFacade(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService) {
		super();
		this.executionState = executionState;
		this.actionExecutor = actionExecutor;
		this.context = context;
		this.eventService = eventService;
		delegate = new WorkflowSessionImpl(executionState, actionExecutor, context, eventService);
	}
	
	@Override
	public void execute(String transition) throws Exception {
		
		final State currentState = executionState.getCurrentState();
		
		WorkflowSession subSession = createSubSession(currentState);
		processSession(subSession, transition);
		
		if (subSession == null || subSession.isComplete()) {
			if (null != transition) {
				delegate.execute(transition);
			} else {
				delegate.execute();
			}
			
		} 
		
	}

	@Override
	public void execute() throws Exception {
		
		execute(null);
		
	}
	
	private void processSession(WorkflowSession subSession, String transition) throws Exception {
		if (subSession != null && !subSession.isComplete()) {
			if (null != transition) {
				subSession.execute(transition);
			} else {
				subSession.execute();
			}
			
		}
	}
	
	private WorkflowSession createSubSession(State targetState) {
		SubProcessRole subProcessRole = targetState.getRole(SubProcessRole.class);
		if(subProcessRole != null) {
			ExecutionState subState = getExecutionState(executionState.getChildren(), subProcessRole.getSubProcessId());
			
			// load new sub-session to cover the process node - it may complete to the end automatically so let this function complete
			WorkflowSession subSession = new WorkflowSessionImpl(subState, actionExecutor, context, eventService);
			return subSession;
		}
		return null;
	}
	
	private ExecutionState getExecutionState(List<ExecutionState> processStates, final String id) {
		List<ExecutionState> subProcessStates = FunctionUtils.predicateCollection(processStates, new Predicate<ExecutionState>() {
			@Override
			public boolean accept(ExecutionState state) {
				return state.getId().equals(id);
			}});
		return subProcessStates.size() == 1 ? subProcessStates.get(0) : null;
	}

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public ExecutionState getExecutionState() {
		return this.delegate.getExecutionState();
	}

	@Override
	public boolean isComplete() {
		return delegate.isComplete();
	}



}
