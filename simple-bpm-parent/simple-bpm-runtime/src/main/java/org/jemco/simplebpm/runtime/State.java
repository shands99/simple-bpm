package org.jemco.simplebpm.runtime;

import java.util.List;

public interface State {
	
	boolean isEnd();
	
	void setEnd(boolean isEnd);
	
	boolean isBlocking();
	
	void setBlocking(boolean blocking);
	
	String getName();
	
	boolean validate();
	
	void addStateRole(StateRole role);
	
	<T extends StateRole> T getRole(Class<T> roleType);
	
	List<StateTransition> getExitTransitions();
	
}
