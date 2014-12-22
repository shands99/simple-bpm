package org.jemco.simplebpm.runtime;

public interface StateTransition {

	String getName();
	
	State getTargetState();
	
	GuardPredicate getGuardPredicate();
	
}
