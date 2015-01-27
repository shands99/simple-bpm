package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateTransition;

public interface ExecutionState {

	String getId();
	
	State getStart();
	
	State getCurrentState();
			
	State executeTransition(StateTransition transition);
	
	State getPrevious();
	
}
