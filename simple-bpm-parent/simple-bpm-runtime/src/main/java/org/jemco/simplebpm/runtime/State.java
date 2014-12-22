package org.jemco.simplebpm.runtime;

import java.util.List;

public interface State {
	
	boolean isEnd();
	
	boolean isBlocking();
	
	String getName();
	
	boolean validate();
	
	void addTransition(StateTransition transition);
	
	StateTransition getTransition(String name);
		
	List<StateTransition> getExitTransitions();
	
}
