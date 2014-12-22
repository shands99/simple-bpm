package org.jemco.simplebpm.proxy;

import java.lang.reflect.Proxy;

import org.jemco.simplebpm.WorkflowSession;

public class ProxyWorkflowServiceImpl implements ProxyWorkflowService {

	@Override
	public WorkflowSession newSession(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ProxyWorkflow> T getWorkflowProxy(String id, Class<T> type) {
		
		WorkflowSession session = newSession(id);
		
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
