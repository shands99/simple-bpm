package org.jemco.simplebpm;

import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;

public class BaseRegistryService implements HasRegistry {

	protected Registry registry;

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
	@Override
	public void loadService() {
		// Override if required
	}
	
}
