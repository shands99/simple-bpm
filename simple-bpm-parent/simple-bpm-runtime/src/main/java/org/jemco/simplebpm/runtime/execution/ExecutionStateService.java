package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Process;

public interface ExecutionStateService extends ProcessManager {
	
	void setRegistry(Registry registry);
	
	ExecutionState getExecutionContext(String id);

	ExecutionState newExecutionContext(String id, Process processName);
	
}
