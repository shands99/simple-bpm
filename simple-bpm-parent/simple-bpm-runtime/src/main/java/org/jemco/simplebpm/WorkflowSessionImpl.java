package org.jemco.simplebpm;

import java.text.MessageFormat;
import java.util.List;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.Phase;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.runtime.ActionExecutorRole;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateTransition;
import org.jemco.simplebpm.runtime.events.NodeTransitionEvent;
import org.jemco.simplebpm.runtime.execution.Context;
import org.jemco.simplebpm.runtime.execution.ExecutionState;

/**
 * The workflow session manages the execution path and dictates the how of the execution flow. This is central to the BPM system and is the most important class.
 * TODO parallell executions, sub-processes, decision states.
 * @author a583548
 *
 */
final class WorkflowSessionImpl implements WorkflowSession {

	private final ExecutionState executionState;
	
	private final ActionExecutor actionExecutor;
	
	private final Context context;
	
	private final EventService eventService;

	// TODO move to external property file
	private static final String MSG_END_STATE = "Current state {0} is a final state. Unable to take transition {1}.";
	private static final String MSG_NOT_VALID_TRANS = "Unable to take transition {0} from state {1}.";
	private static final String MSG_MORE_THAN_ONE_DEFAULT = "More than one default transition from node {0}.";
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService) {
		super();
		this.executionState = executionState;
		this.actionExecutor = actionExecutor;
		this.context = context;
		this.eventService = eventService;
	}

	public void execute(final String transition) throws Exception {

		final State currentState = executionState.getCurrentState();
		
		// cannot progress from final state
		if (currentState.isEnd()) {
			throw new WorkflowException(MessageFormat.format(MSG_END_STATE, currentState.getName(), transition));
		}
		
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
	
	private void executeTransition(StateTransition targetTransition) throws WorkflowException {
		
		// retrieve target state
		State targetState = targetTransition.getTargetState();
		
		// check if target state has actions.
		ActionExecutorRole actionRole = targetState.getRole(ActionExecutorRole.class);
		if (actionRole != null) {
			actionExecutor.executeActions(actionRole, this, Phase.IN);
		}
		
		// finalise and update execution context
		executionState.executeTransition(targetTransition);
		
		// on transition execute any exit handlers
		if (actionRole != null) {
			actionExecutor.executeActions(actionRole, this, Phase.OUT);
		}
		
		// raise event for node transition.
		eventService.raiseEvent(new NodeTransitionEvent(this.context, executionState.getPrevious()
				, executionState.getCurrentState(), targetTransition));
		
		// if new current state is non-blocking it should auto transition.
		while (!executionState.getCurrentState().isBlocking() 
				&& !executionState.getCurrentState().isEnd()) 
		{
			
			// if non-blocking it can either have 1 non guarded transition, or n transitions where some are guarded but only 1 default can be non-guarded.
			// execute each in turn to find if any pass, first that passes take the transition
			List<StateTransition> guardedTransitions = FunctionUtils.predicateCollection(executionState.getCurrentState().getExitTransitions(), new Predicate<StateTransition>(){
				public boolean accept(StateTransition object) {
					return object.getGuardPredicate() != null;
				}});
			
			StateTransition newTargetTransition = executeGuardConditions(guardedTransitions);
			if (newTargetTransition == null) {
				
				List<StateTransition> defaultTransitions = FunctionUtils.predicateCollection(executionState.getCurrentState().getExitTransitions(), new Predicate<StateTransition>(){
					public boolean accept(StateTransition object) {
						return object.getGuardPredicate() == null;
					}});
				
				// cannot have more than one default transition
				if (defaultTransitions.size() > 1) {
					throw new WorkflowException(MessageFormat.format(MSG_MORE_THAN_ONE_DEFAULT, executionState.getCurrentState().getName()));
				}
				
				if (defaultTransitions.size() == 1) {
					newTargetTransition = defaultTransitions.get(0);
				}
				
			}
			
			// This effectively loops through the execution flow until it errors, reaches a blocking state or final state.
			if (newTargetTransition != null) {
				executeTransition(newTargetTransition);
			} else {
				// TODO should we error here ?
			}
			
		}
		
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
	
}
