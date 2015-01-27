package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.runtime.Process;

public interface ProcessManager {

	Process create(String name);
	
	Process retrieve(String name);
	
}
