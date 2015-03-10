package org.jemco.simplebpm;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.ExecutionState;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Context;
import org.jemco.simplebpm.runtime.DefaultContext;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.jemco.simplebpm.runtime.WorkflowSessionImpl;
import org.jemco.simplebpm.utils.Assert;

public class DefaultWorkflowService implements WorkflowService, HasRegistry {

	private ExecutionStateService executionContextService;
		
	private ActionExecutor actionExecutor;
	
	private EventService eventService;
	
	private Registry registry;
	
	@Override
	public void loadService() {
		if (registry != null) {
			executionContextService = registry.get(ExecutionStateService.class);
			actionExecutor = registry.get(ActionExecutor.class);
			eventService = registry.get(EventService.class);
		}
	}
	
	@Override
	public WorkflowSession newSession(String id, String processName) {
		
		Process process = executionContextService.getProcessManager().retrieve(processName);
		return newSession(id, process);
		
	}

	@Override
	public WorkflowSession newSession(String id, Process process) {
		
		return this.newSession(id, process, new DefaultContext(registry));
		
	}
	
	public WorkflowSession newSession(String id, Process process, Context context) {
		
		Assert.notNull(process, "Process is null.");
		
		// if no id is provided assume this will always be a brand new context
		ExecutionState executionContext = executionContextService.newExecutionContext(id, process);
		
		return new WorkflowSessionImpl(executionContext, actionExecutor, context, eventService, process);
		
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

}
