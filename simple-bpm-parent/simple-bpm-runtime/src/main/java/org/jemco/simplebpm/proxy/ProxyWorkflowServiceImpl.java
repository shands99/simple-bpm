package org.jemco.simplebpm.proxy;

import java.lang.reflect.Proxy;

import org.jemco.simplebpm.WorkflowSession;
import org.springframework.util.Assert;

public class ProxyWorkflowServiceImpl implements ProxyWorkflowService {

	@Override
	public WorkflowSession newSession(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends ProxyWorkflow> T getWorkflowProxy(String id, Class<T> type) {
		
		WorkflowSession session = newSession(id);
		Assert.notNull(session, "No session..");
		
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
