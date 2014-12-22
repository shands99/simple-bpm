package org.jemco.simplebpm;

import org.jemco.simplebpm.runtime.Context;
import org.jemco.simplebpm.runtime.execution.ExecutionContext;

public interface WorkflowSession {
	
	/**
	 * Execute workflow using the named transition.
	 * @param transition
	 * @throws Exception
	 */
	void execute(String transition) throws Exception;
	
	Context getContext();
	
	ExecutionContext getExecutionState();
		
}
