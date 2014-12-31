package org.jemco.simplebpm.runtime;

public interface StateTransition extends ValidatingEntity {

	String getName();
	
	State getTargetState();
	
	GuardPredicate getGuardPredicate();
	
}
