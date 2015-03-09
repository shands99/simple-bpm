package org.jemco.simplebpm.runtime.components;

import org.jemco.simplebpm.process.State;

public class ExclusiveChoiceNodeComponent extends BaseNodeComponent {

	private ExclusiveChoiceNodeProcessor nodeProcessor = new ExclusiveChoiceNodeProcessor();
	
	@Override
	public NodeProcessor getNodeProcessor(State state) {
		return nodeProcessor;
	}

}
