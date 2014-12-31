package org.jemco.simplebpm.runtime;

import java.util.List;

public interface State extends ValidatingEntity {
	
	boolean isEnd();
	
	void setEnd(boolean isEnd);
	
	boolean isBlocking();
	
	void setBlocking(boolean blocking);
	
	String getName();
		
	void addStateRole(StateRole role);
	
	<T extends StateRole> T getRole(Class<T> roleType);
	
	List<StateTransition> getExitTransitions();
	
}
