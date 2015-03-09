package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.process.State;

public final class SessionUtils {

	private SessionUtils(){}
	
	public static State getCurrentNode(WorkflowSession session) {
		return (session != null && session.getProcess() != null && session.getExecutionState() != null) 
				? session.getProcess().getState(session.getExecutionState().getCurrentToken().getName()) : null;
	}
	
}
