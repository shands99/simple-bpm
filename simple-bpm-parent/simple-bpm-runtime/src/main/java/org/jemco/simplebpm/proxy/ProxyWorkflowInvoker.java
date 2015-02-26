package org.jemco.simplebpm.proxy;

import java.lang.reflect.Proxy;

import org.jemco.simplebpm.WorkflowService;
import org.jemco.simplebpm.runtime.WorkflowSession;

public class ProxyWorkflowInvoker implements WorkflowInvoker {

	private WorkflowService worklflowService;

	@Override
	public <T extends ProxyWorkflow> T getWorkflowProxy(String id, Class<T> type) {
		
		WorkflowSession session = worklflowService.newSession(id, type.getSimpleName());
		
		@SuppressWarnings("unchecked")
		T proxyInstance = (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
				new Class[]{type}, new WorkflowInvocationHandler(session));
	
		return proxyInstance;
		
	}

	@Override
	public <T extends ProxyWorkflow> T getWorkflowProxy(Class<T> type) {
		
		
		
		
		
		
		return null;
	}

}
