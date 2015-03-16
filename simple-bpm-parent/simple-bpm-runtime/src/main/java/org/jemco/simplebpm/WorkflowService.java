package org.jemco.simplebpm;

import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.runtime.Context;
import org.jemco.simplebpm.runtime.WorkflowSession;

public interface WorkflowService {

	/**
	 * Create a new session for the given execution id.
	 * @param id
	 * @return
	 */
	WorkflowSession newSession(String id, String processName);
	
	/**
	 * Create a new session for the given execution id.
	 * @param id
	 * @return
	 */
	WorkflowSession newSession(String id, Process process);
	
	WorkflowSession newSession(String id, Process process, Context context);
	
	WorkflowSession newSession(Process process, Context context, ExecutionState executionState);
	
}
