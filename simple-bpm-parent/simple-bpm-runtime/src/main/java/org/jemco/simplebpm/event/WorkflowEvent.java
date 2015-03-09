package org.jemco.simplebpm.event;

import org.jemco.simplebpm.runtime.Context;

public interface WorkflowEvent extends Event {

	Context getContext();
	
}
