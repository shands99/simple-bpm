package org.jemco.simplebpm.runtime;

class StateTransitionImpl implements StateTransition {

	private String name;
	
	private State sourceState;
	
	private State targetState;
	
	private GuardPredicate guardPredicate;
	
	StateTransitionImpl(String name, State sourceState,
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateTransitionImpl other = (StateTransitionImpl) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public State getSourceState() {
		return sourceState;
	}

	
	
	

}
