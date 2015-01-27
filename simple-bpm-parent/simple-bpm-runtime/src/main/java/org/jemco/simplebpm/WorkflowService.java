package org.jemco.simplebpm;

public interface WorkflowService {

	/**
	 * Create a new session for the given execution id.
	 * @param id
	 * @return
	 */
	WorkflowSession newSession(String id, String processName);
	
}
