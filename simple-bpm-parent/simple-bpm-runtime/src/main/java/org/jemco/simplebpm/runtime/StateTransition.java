package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.runtime.guard.GuardPredicate;

public interface StateTransition extends ValidatingEntity {

	String getName();
	
	State getTargetState();
	
	GuardPredicate getGuardPredicate();
	
}
