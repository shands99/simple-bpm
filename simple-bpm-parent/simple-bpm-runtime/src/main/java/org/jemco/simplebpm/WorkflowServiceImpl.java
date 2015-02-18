package org.jemco.simplebpm;

import java.text.MessageFormat;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Process;
import org.jemco.simplebpm.runtime.execution.ExecutionState;
import org.jemco.simplebpm.runtime.execution.ExecutionStateService;
import org.jemco.simplebpm.utils.Assert;

public class WorkflowServiceImpl implements WorkflowService {

	private ExecutionStateService executionContextService;
		
	private ActionExecutor actionExecutor;
	
	private Registry registry;

	private void initServices() {
		
		executionContextService = registry.get(ExecutionStateService.class);
		Assert.notNull(executionContextService, "");
		
		actionExecutor = registry.get(ActionExecutor.class);
		Assert.notNull(actionExecutor, "");
				
	}
		
	@Override
	public WorkflowSession newSession(String id, String processName) {
		// TODO Check if in sub-process node if so return sub execution state with session
		
		return null;
	}
	
	public WorkflowSession newSession(String processName) 
	{
		
		// Lookup process - fail if not found
		Process process = executionContextService.retrieve(processName);
		Assert.notNull(process, MessageFormat.format("Process {0} is null.", processName));
		
		// if no id is provided assume this will always be a brand new context
		ExecutionState executionContext = executionContextService.newExecutionContext(null, process);
		
		return null;
	}

	

}
