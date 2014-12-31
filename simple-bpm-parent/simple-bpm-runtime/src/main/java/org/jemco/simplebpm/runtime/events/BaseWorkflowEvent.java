package org.jemco.simplebpm.runtime.events;

import org.jemco.simplebpm.event.BaseEvent;
import org.jemco.simplebpm.event.WorkflowEvent;
import org.jemco.simplebpm.runtime.Context;

public abstract class BaseWorkflowEvent extends BaseEvent implements WorkflowEvent {

	private Context context;
	
	public static final String WORKFLOW_GROUP = "WORKFLOW";
			
	public BaseWorkflowEvent(String type, Context context) {
		super(type, WORKFLOW_GROUP);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

}
