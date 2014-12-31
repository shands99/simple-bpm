package org.jemco.simplebpm.runtime.events;

import org.jemco.simplebpm.runtime.Context;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateTransition;

public class NodeTransitionEvent extends BaseWorkflowEvent {

	public static final String EVENT_TYPE = "NodeTransitionEvent";
	
	private State from;
	
	private State to;
	
	private StateTransition transition;

	public NodeTransitionEvent(Context context, State from,
			State to, StateTransition transition) {
		super(EVENT_TYPE, context);
		this.from = from;
		this.to = to;
		this.transition = transition;
	}
	
	public State getFrom() {
		return from;
	}

	public State getTo() {
		return to;
	}

	public StateTransition getTransition() {
		return transition;
	}

}
