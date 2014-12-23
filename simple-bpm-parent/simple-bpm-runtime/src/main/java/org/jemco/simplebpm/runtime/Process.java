package org.jemco.simplebpm.runtime;

/**
 * Process is the root entity for a business process. All states and transitions should be added here.
 * <pr/>
 * 
 * @author a583548
 *
 */
public interface Process {
	
	String getName();
	
	State getState(String name);
	
	State addStartState(String name);
	
	State addState(String name, boolean isBlocking, boolean isEnd);
	
	State addTransition(String sourceState, String transitionName, String targetState);
	
	State addTransition(State sourceState, String transitionName, String targetState);
	
}
