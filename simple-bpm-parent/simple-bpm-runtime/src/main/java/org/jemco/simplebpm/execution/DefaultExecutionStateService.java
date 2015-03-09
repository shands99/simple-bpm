package org.jemco.simplebpm.execution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jemco.simplebpm.process.Process;

public class DefaultExecutionStateService implements ExecutionStateService {
	
	private ProcessService processManager;
	
	private Map<String, ExecutionState> stateMap = Collections.synchronizedMap(new HashMap<String, ExecutionState>());
	
	public DefaultExecutionStateService() {
		this.processManager = new DefaultProcessManager();
	}
	
	@Override
	public ExecutionState getExecutionContext(String id) {
		return stateMap.get(id);
	}

	@Override
	public ExecutionState newExecutionContext(String id, Process process) 
	{		
		ExecutionState context = new DefaultRamExecutionState(id, process.getStartState());
		if (context != null) {
			stateMap.put(id, context);
		}
		
		return context;
		
	}
	
	public ProcessService getProcessManager() {
		return processManager;
	}


}
