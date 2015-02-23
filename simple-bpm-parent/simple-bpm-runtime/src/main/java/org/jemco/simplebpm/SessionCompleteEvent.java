package org.jemco.simplebpm;

import org.jemco.simplebpm.runtime.events.BaseWorkflowEvent;
import org.jemco.simplebpm.runtime.execution.Context;
import org.jemco.simplebpm.runtime.execution.ExecutionState;

public class SessionCompleteEvent extends BaseWorkflowEvent {

	public static final String EVENT_TYPE = "SessionCompleteEvent";
	
	private ExecutionState executionState;
	
	public SessionCompleteEvent(Context context, ExecutionState executionState) {
		super(EVENT_TYPE, context);
		this.executionState = executionState;
	}

	public ExecutionState getExecutionState() {
		return executionState;
	}

}
