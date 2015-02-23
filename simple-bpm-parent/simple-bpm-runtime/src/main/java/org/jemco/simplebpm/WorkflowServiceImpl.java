package org.jemco.simplebpm;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Process;
import org.jemco.simplebpm.runtime.execution.Context;
import org.jemco.simplebpm.runtime.execution.DefaultContext;
import org.jemco.simplebpm.runtime.execution.ExecutionState;
import org.jemco.simplebpm.runtime.execution.ExecutionStateService;
import org.jemco.simplebpm.utils.Assert;

public class WorkflowServiceImpl implements WorkflowService {

	private ExecutionStateService executionContextService;
		
	private ActionExecutor actionExecutor;
	
	private EventService eventService;
	
	private Registry registry;

	private void initServices() {
		
		executionContextService = registry.get(ExecutionStateService.class);
		Assert.notNull(executionContextService, "");
		
		actionExecutor = registry.get(ActionExecutor.class);
		Assert.notNull(actionExecutor, "");
				
		eventService = registry.get(EventService.class);
		Assert.notNull(eventService, "");
		
	}
		
	@Override
	public WorkflowSession newSession(String id, String processName) {
		
		Process process = this.executionContextService.retrieve(processName);
		return newSession(id, process);
		
	}

	@Override
	public WorkflowSession newSession(String id, Process process) {
		
		Assert.notNull(process, "Process is null.");
		
		// if no id is provided assume this will always be a brand new context
		ExecutionState executionContext = executionContextService.newExecutionContext(id, process);
		Context context = new DefaultContext(registry);
		
		return new WorkflowSessionFacade(executionContext, actionExecutor, context, eventService, id);
		
	}

	

}
