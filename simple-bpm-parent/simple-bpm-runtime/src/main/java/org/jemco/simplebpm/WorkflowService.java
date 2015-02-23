package org.jemco.simplebpm;

import org.jemco.simplebpm.runtime.Process;

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
	
}
