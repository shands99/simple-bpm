package org.jemco.simplebpm.execution;

import org.jemco.simplebpm.process.Process;

public interface ExecutionStateService {
	
	ExecutionState getExecutionContext(String id);

	ExecutionState newExecutionContext(String id, Process processName);
	
	ProcessService getProcessManager();
		
}
