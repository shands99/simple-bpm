package org.jemco.simplebpm;

import java.util.ArrayList;
import java.util.List;

import org.jemco.simplebpm.action.ActionExecutor;
import org.jemco.simplebpm.action.DefaultActionExecutor;
import org.jemco.simplebpm.event.DefaultEventService;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.execution.DefaultExecutionStateService;
import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.process.DefaultProcessService;
import org.jemco.simplebpm.process.ProcessService;
import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.utils.ServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceLoaderImpl implements ServiceLoader {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceLoaderImpl.class);
	
	private static final String MSG_NOTFOUND_REG = "Type {0} not found in registry. Using SPI to load service.";
	
	private static final String MSG_SVCS_LOADED = "Services already loaded for this application.";

	private List<Object> services = new ArrayList<Object>();
	
	private EventService eventService;
	
	private WorkflowService workflowService;
	
	private ActionExecutor actionExecutor;
	
	private ExecutionStateService executionStateService;
	
	private ProcessService processService;
	
	private Registry registry;
	
	public ServiceLoaderImpl(Registry registry) {
		super();
		this.registry = registry;
	}
	
	@Override
	public void load() throws WorkflowException {
		
		// mechanism to check that services have already been loaded for this application
		if (services.size() > 0) {
			throw new WorkflowException(MSG_SVCS_LOADED);
		}
		
		loadEventService();
		loadExecutionService();
		loadActionExecutor();
		loadWorkflowService();
		loadProcessService();
		
	}
	
	private void loadProcessService() {
		this.processService = loadWithRegistry(ProcessService.class);
		if (processService == null) {
			processService = new DefaultProcessService();
			services.add(processService);
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

	public List<Object> getServices() {
		return services;
	}

	
	
}
