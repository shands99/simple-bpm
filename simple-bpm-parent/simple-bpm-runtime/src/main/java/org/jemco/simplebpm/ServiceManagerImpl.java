package org.jemco.simplebpm;

import java.util.List;

import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;

public class ServiceManagerImpl implements ServiceManager {

	private Registry registry;
		
	public ServiceManagerImpl(Registry registry) {
		super();
		this.registry = registry;
	}
	
	@Override
	public void startAll(List<Object> services) {
		
		if (services != null) {
			for (Object svc : services) {
				postProcessService(svc);
			}
		}
		
	}
	
	private void postProcessService(Object service) {
		if (service instanceof HasRegistry) {
			((HasRegistry) service).setRegistry(registry);
			((HasRegistry) service).loadService();
		}
	}

	

}
