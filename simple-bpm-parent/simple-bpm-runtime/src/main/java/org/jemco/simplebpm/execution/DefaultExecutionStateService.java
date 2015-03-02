package org.jemco.simplebpm.execution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.SubProcessRole;

public class DefaultExecutionStateService implements ExecutionStateService {
	
	private ProcessManager processManager;
	
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
		
		for (State state : process.getStates()) {
			
			SubProcessRole sub = state.getRole(SubProcessRole.class);
			if (sub != null) {
				String subId = sub.getSubProcess().getName() +":"+id;
				context.getChildren().add(new DefaultRamExecutionState(subId, sub.getSubProcess().getStartState()));
			}
			
		}
		
		if (context != null) {
			stateMap.put(id, context);
		}
		
		return context;
		
	}
	
	public ProcessManager getProcessManager() {
		return processManager;
	}


}
