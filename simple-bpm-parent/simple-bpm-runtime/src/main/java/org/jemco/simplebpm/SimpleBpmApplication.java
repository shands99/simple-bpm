package org.jemco.simplebpm;

import org.jemco.simplebpm.event.DefaultEventService;
import org.jemco.simplebpm.event.EventService;
import org.jemco.simplebpm.registry.DefaultRegistry;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(SimpleBpmApplication.class);
	
	private static final String MSG_NOTFOUND_REG = "Type {0} not found in registry. Using SPI to load service.";
	
	public void start() {
		loadRegistry();
		loadEventService();
	}
	
	private <T> T loadWithRegistry(Class<T> type) {
		
		// always check registry first - could be using external CDI or Spring context.
		T service = this.registry.get(type);
		if (service == null) {
			LOG.info(MSG_NOTFOUND_REG, type);
			service = ServiceUtils.load(type);
		}
		return service;
	}
	
	private void loadEventService() {
		eventService = loadWithRegistry(EventService.class);
		if (eventService == null) {
			eventService = new DefaultEventService();
		}
	}
	
	protected void loadRegistry() {
		
		registry = ServiceUtils.load(Registry.class);
		if (registry == null) {
			registry = new DefaultRegistry();
		}
		
	}
	
}
