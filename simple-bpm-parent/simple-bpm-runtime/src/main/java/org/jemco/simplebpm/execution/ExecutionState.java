package org.jemco.simplebpm.execution;

import java.util.List;

import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.StateTransition;

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
	
	State getStart();
	
	State getCurrentState();
			
	State executeTransition(StateTransition transition);
	
	State getPrevious();
	
}
