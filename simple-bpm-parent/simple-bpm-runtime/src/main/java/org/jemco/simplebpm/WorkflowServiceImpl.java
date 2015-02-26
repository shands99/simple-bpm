package org.jemco.simplebpm;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.Context;
import org.jemco.simplebpm.execution.DefaultContext;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.jemco.simplebpm.runtime.WorkflowSessionFacade;
import org.jemco.simplebpm.utils.Assert;

public class WorkflowServiceImpl implements WorkflowService {

	private ExecutionStateService executionContextService;
		
	private ActionExecutor actionExecutor;
	
	private EventService eventService;
	
	private Registry registry;
		
	public WorkflowServiceImpl(ExecutionStateService executionContextService,
			ActionExecutor actionExecutor, EventService eventService,
			Registry registry) {
		super();
		this.executionContextService = executionContextService;
		this.actionExecutor = actionExecutor;
		this.eventService = eventService;
		this.registry = registry;
	}
	
	@Override
	public WorkflowSession newSession(String id, String processName) {
		
		Process process = executionContextService.getProcessManager().retrieve(processName);
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
