package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.runtime.Process;

public interface ProcessManager {

	Process save(String name);
	
	Process retrieve(String name);
	
}
