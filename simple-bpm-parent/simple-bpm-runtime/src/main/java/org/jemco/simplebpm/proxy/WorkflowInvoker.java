package org.jemco.simplebpm.proxy;

public interface WorkflowInvoker {

	<T extends ProxyWorkflow> T getWorkflowProxy(String id, Class<T> type);
	
	/**
	 * If you are using atomic, non - persistent processes, you can use this method.
	 * @param type
	 * @return
	 */
	<T extends ProxyWorkflow> T getWorkflowProxy(Class<T> type);
	
}
