package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.process.FlowNodeRole;
import org.jemco.simplebpm.process.State;

public class FlowNodeComponent extends BaseNodeComponent {

	private FlowNodeProcessor flowNodeProcessor = new FlowNodeProcessor();
	
	@Override
	public NodeProcessor getNodeProcessor(State state) {
		return state.getRole(FlowNodeRole.class) != null ? flowNodeProcessor : null;
	}

}
