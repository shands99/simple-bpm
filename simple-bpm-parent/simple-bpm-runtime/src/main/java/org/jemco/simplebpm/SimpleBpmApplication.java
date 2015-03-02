package org.jemco.simplebpm;

import java.util.ArrayList;
import java.util.List;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.DefaultActionExecutor;
import org.jemco.simplebpm.event.DefaultEventService;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.DefaultExecutionStateService;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.registry.DefaultRegistry;
import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for loading the SPI based services.
 * @author A583548
 *
 */
public class SimpleBpmApplication {

	protected Registry registry;
	
	private EventService eventService;
	
	private WorkflowService workflowService;
	
	private ActionExecutor actionExecutor;
	
	private ExecutionStateService executionStateService;
	
	private static final Logger LOG = LoggerFactory.getLogger(SimpleBpmApplication.class);
	
	private static final String MSG_NOTFOUND_REG = "Type {0} not found in registry. Using SPI to load service.";
	
	private List<Object> services = new ArrayList<Object>();
	
	public synchronized final void start() {
		
		loadRegistry();
		loadEventService();
		loadExecutionService();
		loadActionExecutor();
		loadWorkflowService();
		
		for (Object svc : this.services) {
			postProcessService(svc);
		}
		
	}
	
	private void loadWorkflowService() {
		this.workflowService = loadWithRegistry(WorkflowService.class);
		if (workflowService == null) {
			workflowService = new DefaultWorkflowService();
			services.add(workflowService);
		}
	}
	
	private void loadActionExecutor() {
		this.actionExecutor = loadWithRegistry(ActionExecutor.class);
		if (actionExecutor == null) {
			actionExecutor = new DefaultActionExecutor();
			services.add(actionExecutor);
		}
	}
	
	private void loadExecutionService() {
		this.executionStateService = loadWithRegistry(ExecutionStateService.class);
		if (executionStateService == null) {
			executionStateService = new DefaultExecutionStateService();
			services.add(executionStateService);
		}
	}
	
	private void loadEventService() {
		eventService = loadWithRegistry(EventService.class);
		if (eventService == null) {
			eventService = new DefaultEventService();
			services.add(eventService);
		}
	}
	
	private void postProcessService(Object service) {
		if (service instanceof HasRegistry) {
			((HasRegistry) service).setRegistry(registry);
			((HasRegistry) service).loadService();
		}
	}
	
	protected void loadRegistry() {
		
		//albeit unlikely the registry can be loaded via SPI.
		registry = ServiceUtils.load(Registry.class);
		if (registry == null) {
			registry = new DefaultRegistry();
		}
		
	}

	private <T> T loadWithRegistry(Class<T> type) {
		
		// always check registry first - could be using external CDI or Spring context.
		T service = registry.get(type);
		if (service == null) {
			LOG.info(MSG_NOTFOUND_REG, type);
			service = ServiceUtils.load(type);
			// add to the registry so it is visible to the other SPI services
			registry.add(service);
			
		}
		services.add(service);
		return service;
	}
	
	public WorkflowService getWorkflowService() {
		return workflowService;
	}

	public ExecutionStateService getExecutionStateService() {
		return executionStateService;
	}
	
}
