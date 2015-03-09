package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.registry.Registry;

public interface NodeComponent {

	NodeProcessor getNodeProcessor(State state);
	
}
