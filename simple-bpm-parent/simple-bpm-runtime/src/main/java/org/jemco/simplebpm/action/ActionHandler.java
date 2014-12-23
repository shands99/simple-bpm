package org.jemco.simplebpm.action;

import org.jemco.simplebpm.WorkflowSession;

public interface ActionHandler {

	int getSalience();
	
	void execute(WorkflowSession session);
	
}
