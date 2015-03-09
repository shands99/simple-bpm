package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.runtime.WorkflowSession;

public interface NodeProcessor {
		
	void handle(WorkflowSession session) throws Exception ;
	
}
