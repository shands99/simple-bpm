package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.execution.ExecutionStateService;
import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.SubProcessRole;

public class SubProcessNodeComponent extends BaseNodeComponent {
	
	private SubNodeProcessor processor;
	
	@Override
	public void loadService() {
		ExecutionStateService executionService = registry.get(ExecutionStateService.class);
		processor = new SubNodeProcessor(executionService);
	}
	
	@Override
	public NodeProcessor getNodeProcessor(State state) {
		if (state.getRole(SubProcessRole.class) != null) {
			return processor;
		}
		return null;
	}
	
}
