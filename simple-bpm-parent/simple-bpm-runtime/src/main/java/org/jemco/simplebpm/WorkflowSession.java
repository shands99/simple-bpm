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
	
	Context getContext();
	
	ExecutionState getExecutionState();
		
}
