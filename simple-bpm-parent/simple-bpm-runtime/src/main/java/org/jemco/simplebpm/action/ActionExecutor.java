package org.jemco.simplebpm.action;

import org.jemco.simplebpm.process.ActionExecutorRole;
import org.jemco.simplebpm.runtime.WorkflowSession;

public interface ActionExecutor {

	/**
	 * Given an action state, execute a series of actions. Check order if provided.
	 * @param state
	 */
	void executeActions(ActionExecutorRole state, WorkflowSession session, Phase phase) throws ActionException;
	
}
