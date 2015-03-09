package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.SubProcessRole;

public class SubProcessNodeComponent extends BaseNodeComponent {
	
	private SubNodeProcessor processor = new SubNodeProcessor();
	
	@Override
	public NodeProcessor getNodeProcessor(State state) {
		if (state.getRole(SubProcessRole.class) != null) {
			return processor;
		}
		return null;
	}

	

}
