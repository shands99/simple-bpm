package org.jemco.simplebpm.execution;

import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.registry.HasRegistry;
import org.jemco.simplebpm.registry.Registry;

public interface ExecutionStateService {
	
	ExecutionState getExecutionContext(String id);

	ExecutionState newExecutionContext(String id, Process processName);
	
	ProcessManager getProcessManager();
		
}
