package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.runtime.events.BaseWorkflowEvent;

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
