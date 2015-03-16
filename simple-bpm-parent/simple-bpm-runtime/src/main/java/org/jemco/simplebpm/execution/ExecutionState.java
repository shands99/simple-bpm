package org.jemco.simplebpm.execution;

import java.util.List;

public interface ExecutionState {

	/**
	 * In a running context this could be a parent process if the node is a process node, or a parallel flow. 
	 * @return
	 */
	ExecutionState getParent();
	
	/**
	 * Child execution states are either a sub process, or could represent parallel flows. In the case of a parallel flow
	 * you should only expect one child. Parallel flows can have n children.  
	 * @return
	 */
	List<ExecutionState> getChildren();
	
	String getId();
	
	Token getCurrentToken();
	
	Token getPreviousToken();
	
	void createToken(String name, boolean blocking, boolean end);
		
}
