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
	
	private String processId;
	
	public WorkflowSessionFacade(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService, String processId) {
		super();
		this.executionState = executionState;
		this.actionExecutor = actionExecutor;
		this.context = context;
		this.eventService = eventService;
		this.processId = processId;
		delegate = new WorkflowSessionImpl(executionState, actionExecutor, context
				, eventService, new FinaliseCallback<WorkflowSession>() {

					@Override
					public void onFinalise(WorkflowSession target) {
						// this handles the loop when the child execution state may cause the parent session to transition automatically
						if (target.isComplete()) {
							
							
							try {
								execute();
							} catch (Exception e) {
								//TODO think about this
								throw new RuntimeException(e);
							}
						}
					}});
		
	}
	
	private WorkflowSession checkSubSessionActive(State state, String transition) throws Exception {
		WorkflowSession subSession = createSubSession(state);
		processSession(subSession, transition);
		return subSession;
	}
	
	@Override
	public void execute(String transition) throws Exception {
		
		final State currentState = executionState.getCurrentState();
		
		WorkflowSession subSession = createSubSession(currentState);
		// use to see if state of any sub-session moves to complete within this context
		boolean wasComplete = subSession != null ? subSession.isComplete() : false;
		processSession(subSession, transition);
		
		// was there an open sub process at the start of this session that has now completed ?
		if (subSession != null && (wasComplete == false && subSession.isComplete())) {
			
			// set parent state to non-blocking as the child process is now complete
			//TODO fix this for multi threading
			currentState.setBlocking(false);
			processSession(delegate, null);
			
		} else {
			
			processSession(delegate, transition);
			
			// final check to see if we have entered into a sub-process, if so signal.
			checkSubSessionActive(executionState.getCurrentState(), null);
			
		}
		
	}

	@Override
	public void execute() throws Exception {
		
		execute(null);
		
	}
		
	private void processSession(WorkflowSession session, String transition) throws Exception {
		if (session != null && !session.isComplete()) {
			if (null != transition) {
				session.execute(transition);
			} else {
				session.execute();
			}
		} else {
			// Log ?
		}
		
	}
	
	private WorkflowSession createSubSession(State targetState) {
		SubProcessRole subProcessRole = targetState.getRole(SubProcessRole.class);
		if(subProcessRole != null) {
			String id = subProcessRole.getProcessName() + ":" + this.processId;
			ExecutionState subState = getExecutionState(executionState.getChildren(), id);
			
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
