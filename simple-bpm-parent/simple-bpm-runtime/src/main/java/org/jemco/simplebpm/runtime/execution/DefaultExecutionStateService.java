package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Process;
import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.SubProcessRole;

public class DefaultExecutionStateService implements ExecutionStateService {

	private Registry registry;
	
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
	
	private static final String SVC_MSG = "Not implemented in the default service.  You must create your own state management instance.";

	@Override
	public Process save(String name) {
		throw new UnsupportedOperationException(SVC_MSG);
	}

	@Override
	public Process retrieve(String name) {
		throw new UnsupportedOperationException(SVC_MSG);
	}

}
