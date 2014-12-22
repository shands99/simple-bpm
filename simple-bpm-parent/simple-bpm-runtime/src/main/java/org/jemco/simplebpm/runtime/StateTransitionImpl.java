package org.jemco.simplebpm.runtime;

public class StateTransitionImpl implements StateTransition {

	private String name;
	
	private State sourceState;
	
	private State targetState;
	
	private GuardPredicate guardPredicate;
	
	public StateTransitionImpl(String name, State sourceState,
			State targetState) {
		super();
		this.name = name;
		this.sourceState = sourceState;
		this.targetState = targetState;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getTargetState() {
		return targetState;
	}

	public void setTargetState(State targetState) {
		this.targetState = targetState;
	}

	public GuardPredicate getGuardPredicate() {
		return guardPredicate;
	}

	public void setGuardPredicate(GuardPredicate guardPredicate) {
		this.guardPredicate = guardPredicate;
	}

	
	
	

}
