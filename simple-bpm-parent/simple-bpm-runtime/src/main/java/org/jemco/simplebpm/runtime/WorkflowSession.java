package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.process.Process;

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
	
	void execute(boolean overrideBlocked) throws Exception;
	
	Context getContext();
	
	ExecutionState getExecutionState();
	
	/**
	 * Is the underlying process in a final state.
	 * @return
	 */
	boolean isComplete();
	
	ActionExecutor getActionExecutor();
	
	EventService getEventService();
	
	String getSessionId();
	
	Process getProcess();
		
}
