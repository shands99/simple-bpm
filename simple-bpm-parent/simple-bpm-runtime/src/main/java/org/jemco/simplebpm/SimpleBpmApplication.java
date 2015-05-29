package org.jemco.simplebpm;

import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.registry.SimpleHashMapRegistry;
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
	
	private ServiceLoader serviceLoader;
	
	private ServiceManager serviceManager;
		
	private static final Logger LOG = LoggerFactory.getLogger(SimpleBpmApplication.class);
	
	public SimpleBpmApplication() 
	{
		loadRegistry();
		serviceLoader = new ServiceLoaderImpl(registry);
		try {
			serviceLoader.load();
		} catch (WorkflowException e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized final void start() {
		
		serviceManager.startAll(serviceLoader.getServices());
		
	}
	
	protected void loadRegistry() {
		
		//albeit unlikely the registry can be loaded via SPI.
		registry = ServiceUtils.load(Registry.class);
		if (registry == null) {
			registry = new SimpleHashMapRegistry();
		}
		
	}
	
	public WorkflowService getWorkflowService() {
		return this.registry.get(WorkflowService.class);
	}

	public ExecutionStateService getExecutionStateService() {
		return this.registry.get(ExecutionStateService.class);
	}
	
}
