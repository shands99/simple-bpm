package org.jemco.simplebpm;

import org.jemco.simplebpm.runtime.execution.Context;
import org.jemco.simplebpm.runtime.execution.ExecutionState;

public interface WorkflowSession {
	
	/**
	 * Execute workflow using the named transition.
	 * @param transition
	 * @throws Exception
	 */
	void execute(String transition) throws Exception;
	
	/**
	 * Signal the process. This will attempt to auto execute any auto transition states from current.
	 * @throws Exception
	 */
	void execute() throws Exception;
	
	Context getContext();
	
	ExecutionState getExecutionState();
	
	/**
	 * Is the underlying process in a final state.
	 * @return
	 */
	boolean isComplete();
		
}
