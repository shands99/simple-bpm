package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;

public abstract class BaseNodeComponent implements HasRegistry, NodeComponent {

	protected Registry registry;

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
	@Override
	public void loadService() {
		// Override if required
	}
	
}
