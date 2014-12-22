package org.jemco.simplebpm.action;

import org.jemco.simplebpm.runtime.ActionState;

public interface ActionExecutor {

	/**
	 * Given an action state, execute a series of actions. Check order if provided.
	 * @param state
	 */
	void executeActions(ActionState state);
	
}
