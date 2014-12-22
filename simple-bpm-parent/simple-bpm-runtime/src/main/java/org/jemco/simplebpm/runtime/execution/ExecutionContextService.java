package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.runtime.State;

public interface ExecutionContextService {

	ExecutionContext newExecutionContext(State start);
	
	ExecutionContext getExecutionContext(String id);
	
}
