package org.jemco.simplebpm.event;

import org.jemco.simplebpm.execution.Context;

public interface WorkflowEvent extends Event {

	Context getContext();
	
}
