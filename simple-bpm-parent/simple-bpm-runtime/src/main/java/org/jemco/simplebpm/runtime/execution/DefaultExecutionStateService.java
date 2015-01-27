package org.jemco.simplebpm.runtime.execution;

import org.jemco.simplebpm.registry.Registry;
import org.jemco.simplebpm.runtime.Process;

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
		ExecutionState context = new DefaultRamExecutionState(process.getStartState());
		return context;
	}

	@Override
	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	public Process create(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Process retrieve(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
