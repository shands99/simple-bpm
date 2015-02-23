package org.jemco.simplebpm.runtime;

import java.util.Collection;


/**
 * Process is the root entity for a business process. All states and transitions should be added here.
 * <pr/>
 * 
 * @author a583548
 *
 */
public interface Process extends ValidatingEntity {
	
	String getName();
	
	State getState(String name);
	
	Collection<State> getStates();
	
	State addStartState(String name);
	
	State getStartState();
	
	State addState(String name, boolean isBlocking, boolean isEnd);
	
	State addTransition(String sourceState, String transitionName, String targetState);
	
	State addTransition(State sourceState, String transitionName, String targetState);
	
	Process addSubProcess(State parentState, String subProcessName);
	
}
