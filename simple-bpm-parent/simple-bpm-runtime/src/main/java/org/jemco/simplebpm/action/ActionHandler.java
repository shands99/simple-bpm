package org.jemco.simplebpm.action;

import org.jemco.simplebpm.WorkflowSession;

public interface ActionHandler {

	void execute(WorkflowSession session) throws ActionException;
	
}
