package org.jemco.simplebpm.execution;

import org.jemco.simplebpm.process.Process;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.SubProcessRole;
import org.jemco.simplebpm.registry.Registry;

public class DefaultExecutionStateService implements ExecutionStateService {

	private Registry registry;
	
	private ProcessManager processManager;
	
	@Override
	public ExecutionState getExecutionContext(String id) {
		// TODO Auto-generated method stub
		return null;
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
		
		return context;
	}

	@Override
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
	
	
	public ProcessManager getProcessManager() {
		return processManager;
	}



}
