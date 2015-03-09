package org.jemco.simplebpm.runtime;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import org.jemco.simplebpm.WorkflowException;
import org.jemco.simplebpm.action.ActionException;
import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.Phase;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.execution.Token;
import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.process.ActionExecutorRole;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.StateTransition;
import org.jemco.simplebpm.runtime.components.NodeComponent;
import org.jemco.simplebpm.runtime.components.NodeProcessor;
import org.jemco.simplebpm.runtime.events.NodeTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The workflow session manages the execution path and dictates the how of the execution flow. This is central to the BPM system and is the most important class.
 * TODO parallel executions, decision states.
 * @author a583548
 *
 */
public class WorkflowSessionImpl implements WorkflowSession {

	private final String sessionId = UUID.randomUUID().toString();
	
	private final ExecutionState executionState;
	
	private final ActionExecutor actionExecutor;
	
	private final Context context;
	
	private final EventService eventService;
	
	private FinaliseCallback<WorkflowSession> finaliseCallback;
		
	public static final String TRANSITION_CONTEXT_KEY = "Transition";
	
	private List<NodeComponent> nodeComponents;
	
	private Process process;
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowSessionImpl.class);
	
	// TODO move to external property file
	private static final String MSG_END_STATE = "Current state {0} is a final state.";
	private static final String MSG_NOT_VALID_TRANS = "Unable to take transition {0} from state {1}.";
	private static final String MSG_MORE_THAN_ONE_DEFAULT = "More than one default transition from node {0}.";
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService, 
			Process process) {
		this(executionState, actionExecutor, context, eventService,process, null, null);
	}
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService,
			Process process,
			List<NodeComponent> nodeComponents) {
		this(executionState, actionExecutor, context, eventService,process, null, nodeComponents);
	}
	
	public WorkflowSessionImpl(ExecutionState executionState,
			ActionExecutor actionExecutor, Context context,
			EventService eventService,
			Process process,
			FinaliseCallback<WorkflowSession> finaliseCallback,
			List<NodeComponent> nodeComponents) {
		super();
		this.executionState = executionState;
		this.actionExecutor = actionExecutor;
		this.context = context;
		this.eventService = eventService;
		this.finaliseCallback = finaliseCallback;
		this.nodeComponents = nodeComponents;
		this.process = process;
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
		// always reset current transition
		context.addContextVariable(TRANSITION_CONTEXT_KEY, null);
		// attempt to auto transition from current state
		final State currentState = SessionUtils.getCurrentNode(this);
		checkCurrentState(currentState);
		processNode(currentState);
		processAutoTransition(executionState, overrideBlocked);
	}

	
	public void execute(final String transition) throws Exception {
		
		context.addContextVariable(TRANSITION_CONTEXT_KEY, transition);
		
		final State currentState = SessionUtils.getCurrentNode(this);
		checkCurrentState(currentState);
		processNode(currentState);
		
		// transition may change through node processing - use this new one
		final String currentTransition = (String)context.getContextVar(WorkflowSessionImpl.TRANSITION_CONTEXT_KEY);
		// if transition is now null try and use auto transitioning.
		if (null == currentTransition) {
			processAutoTransition(executionState, false);
		} else {
			// retrieve state transition from exit list
			StateTransition targetTransition = FunctionUtils.retrieveObjectFromCollection(currentState.getExitTransitions(), new Predicate<StateTransition>(){
				public boolean accept(StateTransition object) {
					return object.getName().equals(currentTransition);
				}});
			
			// if null - assume this transition cannot be taken from the current state.
			if (targetTransition == null) {
				throw new WorkflowException(MessageFormat.format(MSG_NOT_VALID_TRANS, transition, currentState.getName()));
			}
			
			if (executeGuardCondition(targetTransition))
				executeTransition(targetTransition);
		}
		
	}
		
	/**
	 * If you are inside a node you are in a processing state.  
	 * Custom components can be executed to handle specific workflow behaviour e.g. flow nodes, sub processes, decision states.
	 * @param state
	 * @throws Exception
	 */
	private void processNode(State state) throws Exception {
		// execute node processors
		if (nodeComponents != null) {
			for (NodeComponent component : nodeComponents) {
				NodeProcessor processor = component.getNodeProcessor(state);
				if (processor != null) {
					processor.handle(this);
				}
			}
		}
	}
	
	private void executeTransition(StateTransition targetTransition) throws Exception {
		
		// retrieve target state
		State targetState = targetTransition.getTargetState();
		LOG.info("Attempting to take transition {0}", targetTransition.getName());
		
		// on transition execute any exit handlers of previous state.
		Token previousToken = executionState.getPreviousToken();
		State previous = null;
		if (previousToken != null) {
			previous = process.getState(previousToken.getName());
			executeActions(previous, Phase.OUT);
		}
				
		// finalise and update execution context.  This creates the token to track execution state.
		executionState.createToken(targetState.getName(), targetState.isBlocking(), targetState.isEnd());
				
		// check if target state has actions.
		executeActions(targetState, Phase.IN);
		
		// raise event for node transition.
		eventService.raiseEvent(new NodeTransitionEvent(this.context, previous
				, SessionUtils.getCurrentNode(this), targetTransition));
		
		// at this stage we have transitioned into a new node so are back in a processing state.
		processNode(targetState);
		
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
			while (!executionState.getCurrentToken().isBlocking() 
					&& !executionState.getCurrentToken().isEnd()) 
			{
				retrieveAndProcessTargetTransition(executionState.getCurrentToken());
			}
		} else {
			retrieveAndProcessTargetTransition(executionState.getCurrentToken());
		}
		
	}
	
	private void retrieveAndProcessTargetTransition(Token token) throws Exception {
		State state = process.getState(token.getName());
		StateTransition newTargetTransition = retrieveTargetTransition(state);
		
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
		return executionState.getCurrentToken().isEnd();
	}

	public ActionExecutor getActionExecutor() {
		return actionExecutor;
	}

	public EventService getEventService() {
		return eventService;
	}

	public String getSessionId() {
		return sessionId;
	}

	@Override
	public Process getProcess() {
		return this.process;
	}
	
}
