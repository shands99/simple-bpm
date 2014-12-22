package org.jemco.simplebpm;

import org.jemco.simplebpm.registry.Registry;

public abstract class WorkflowServiceFactory {

	private static WorkflowServiceFactory INSTANCE;
	
	public static WorkflowServiceFactory getWorkflowFactory() {
		return INSTANCE;
	}
	
	private void init() {
		
	}

	public abstract WorkflowService getService();
	
	public Registry getRegistry() {
		return null;
	}
	
}
