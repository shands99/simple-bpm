package org.jemco.simplebpm.runtime;

import java.text.MessageFormat;
import java.util.List;

import org.jemco.simplebpm.WorkflowException;
import org.jemco.simplebpm.action.ActionException;
import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.Phase;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.Context;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.process.ActionExecutorRole;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.StateTransition;
import org.jemco.simplebpm.runtime.events.NodeTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The workflow session manages the execution path and dictates the how of the execution flow. This is central to the BPM system and is the most important class.
 * TODO parallel executions, decision states.
 * @author a583548
 *
 */
class WorkflowSessionImpl implements WorkflowSession {

	private final ExecutionState executionState;
	
	private final ActionExecutor actionExecutor;
	
	private final Context context;
	
	private final EventService eventService;
	
	private FinaliseCallback<WorkflowSession> finaliseCallback;
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowSessionImpl.class);

	// TODO move to external property file
	private static final String MSG_END_STATE = "Current state {0} is a final state.";
	private static final String MSG_NOT_VALID_TRANS = "Unable to take transition {0} from state {1}.";
	private static final String MSG_MORE_THAN_ONE_DEFAULT = "More than one default transition from node {0}.";
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService) {
		this(executionState, actionExecutor, context, eventService, null);
	}
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService,
			FinaliseCallback<WorkflowSession> finaliseCallback) {
		super();
		this.executionState = executionState;
		this.actionExecutor = actionExecutor;
		this.context = context;
		this.eventService = eventService;
		this.finaliseCallback = finaliseCallback;
	}
	
	private void checkCurrentState(State currentState) throws WorkflowException {
		// cannot progress from final state
		if (currentState.isEnd()) {
			throw new WorkflowException(MessageFormat.format(MSG_END_STATE, currentState.getName()));
		}
	}
	
	@Override
	public void execute() throws Exception {
		
		execute(false);
	}
	
	@Override
	public void execute(boolean overrideBlocked) throws Exception {
		// attempt to auto transition from current state
		final State currentState = executionState.getCurrentState();
		checkCurrentState(currentState);
		processAutoTransition(this.executionState, overrideBlocked);
	}

	
	public void execute(final String transition) throws Exception {

		final State currentState = executionState.getCurrentState();
		// TODO check if current state is sub process
		checkCurrentState(currentState);
		
		// retrieve state transition from exit list
		StateTransition targetTransition = FunctionUtils.retrieveObjectFromCollection(currentState.getExitTransitions(), new Predicate<StateTransition>(){
			public boolean accept(StateTransition object) {
				return object.getName().equals(transition);
			}});
		
		// if null - assume this transition cannot be taken from the current state.
		if (targetTransition == null) {
			throw new WorkflowException(MessageFormat.format(MSG_NOT_VALID_TRANS, transition, currentState.getName()));
		}
		
		if (executeGuardCondition(targetTransition))
			executeTransition(targetTransition);
		
	}
	
	private void executeTransition(StateTransition targetTransition) throws Exception {
		
		// retrieve target state
		State targetState = targetTransition.getTargetState();
		LOG.info("Attempting to take transition {0}", targetTransition.getName());
		
		// on transition execute any exit handlers of previous state.
		executeActions(executionState.getPrevious(), Phase.OUT);
				
		// finalise and update execution context
		executionState.executeTransition(targetTransition);
			
		// check if target state has actions.
		executeActions(targetState, Phase.IN);
		
		// raise event for node transition.
		eventService.raiseEvent(new NodeTransitionEvent(this.context, executionState.getPrevious()
				, executionState.getCurrentState(), targetTransition));
		
		processAutoTransition(executionState, false);
		
		// raise session complete event
		eventService.raiseEvent(new SessionCompleteEvent(context, executionState));
		
		// runtime callback on session completion
		if (finaliseCallback != null) {
			finaliseCallback.onFinalise(this);
		}
		
	}
	
	protected void executeActions(State state, Phase phase) throws ActionException {
		if (state != null) {
			ActionExecutorRole role = state.getRole(ActionExecutorRole.class);
			if (role != null) {
				actionExecutor.executeActions(role, this, phase);
			}
		}
	}
	
	/**
	 * This method checks the current state post transition to test if it is a non-blocking state.  If non-blocking it will attempt to 
	 * auto transition to the next state.
	 * @param executionState
	 * @throws Exception 
	 */
	private void processAutoTransition(ExecutionState executionState, boolean overrideBlocked) throws Exception {
		
		if (!overrideBlocked) {
			// if new current state is non-blocking it should auto transition.
			while (!executionState.getCurrentState().isBlocking() 
					&& !executionState.getCurrentState().isEnd()) 
			{
				retrieveAndProcessTargetTransition(executionState.getCurrentState());
			}
		} else {
			retrieveAndProcessTargetTransition(executionState.getCurrentState());
		}
		
	}
	
	private void retrieveAndProcessTargetTransition(State state) throws Exception {
		StateTransition newTargetTransition = retrieveTargetTransition(executionState.getCurrentState());
		
		// This effectively loops through the execution flow until it errors, reaches a blocking state or final state.
		if (newTargetTransition != null) {
			LOG.info("Auto executiing transition {0}", newTargetTransition.getName());
			executeTransition(newTargetTransition);
		} else {
			// TODO should we error here ?
		}
	}
	
	private StateTransition retrieveTargetTransition(State state) throws WorkflowException {
		// if non-blocking it can either have 1 non guarded transition, or n transitions where some are guarded but only 1 default can be non-guarded.
		// execute each in turn to find if any pass, first that passes take the transition
		List<StateTransition> guardedTransitions = FunctionUtils.predicateCollection(state.getExitTransitions(), new Predicate<StateTransition>(){
			public boolean accept(StateTransition object) {
				return object.getGuardPredicate() != null;
			}});
		
		StateTransition newTargetTransition = executeGuardConditions(guardedTransitions);
		if (newTargetTransition == null) {
			
			List<StateTransition> defaultTransitions = FunctionUtils.predicateCollection(state.getExitTransitions(), new Predicate<StateTransition>(){
				public boolean accept(StateTransition object) {
					return object.getGuardPredicate() == null;
				}});
			
			// cannot have more than one default transition
			if (defaultTransitions.size() > 1) {
				throw new WorkflowException(MessageFormat.format(MSG_MORE_THAN_ONE_DEFAULT, state.getName()));
			}
			
			if (defaultTransitions.size() == 1) {
				newTargetTransition = defaultTransitions.get(0);
			}
			
		}
		
		return newTargetTransition;
	}
	
	/**
	 * Execute a list of transition guards and return the first that is accepted.  Note, if there is no guard it assumes acceptance of a notional guard. Use with care.
	 * @param targetTransitions
	 * @return
	 */
	private StateTransition executeGuardConditions(List<StateTransition> targetTransitions) {
		for (StateTransition transition : targetTransitions) {
			if (executeGuardCondition(transition)) {
				return transition;
			}
		}
		return null;
	}
	
	/**
	 * Execute the predicate. Will return true if there is no guard predicate.
	 * @param targetTransition
	 * @return
	 */
	private boolean executeGuardCondition(StateTransition targetTransition) {
		if (targetTransition.getGuardPredicate() != null) {
			return targetTransition.getGuardPredicate().accept(getContext());
		} else {
			return true;
		}
	}
	
	public Context getContext() {
		return context;
	}

	public ExecutionState getExecutionState() {
		return executionState;
	}

	@Override
	public boolean isComplete() {
		return executionState.getCurrentState().isEnd();
	}
	
}
