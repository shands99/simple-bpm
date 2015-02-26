package org.jemco.simplebpm.proxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.jemco.simplebpm.execution.Context;
import org.jemco.simplebpm.runtime.WorkflowSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowInvocationHandler implements InvocationHandler {
	
	private WorkflowSession workflowSession;
	
	private static final Logger LOG = LoggerFactory.getLogger(WorkflowInvocationHandler.class);
	
	public WorkflowInvocationHandler(WorkflowSession workflowSession) {
		super();
		this.workflowSession = workflowSession;
	}

	@Override
	public Object invoke(Object object, Method method, Object[] args)
			throws Throwable {
		
		// Execute special methods.
		for (Method parentMethod : ProxyWorkflow.class.getMethods()) {
			if (parentMethod.equals(method)) {
				method.invoke(new SimpleWorkflowWrapper(), args);
			}
		}
		
		// Add session variables to the context.
		Context context = workflowSession.getContext();
		int i = 0;
		for (Annotation[] parameterAnnotations : method.getParameterAnnotations()) {
			for (Annotation ann : parameterAnnotations) {
				if (ann instanceof ContextVar) 
				{
					if (LOG.isDebugEnabled())
						LOG.info("Adding variable with key {0} and type {1}", new Object[]{((ContextVar) ann).key(), args[i]});;
					context.addContextVariable(((ContextVar) ann).key(), args[i]);
				}
			}
			i++;
		}
		
		// Execute transition
		String transitionName = method.getName();
		workflowSession.execute(transitionName);
		
		return context.getReturnValue();
		
	}
	
	private class SimpleWorkflowWrapper implements ProxyWorkflow {

		@Override
		public WorkflowSession getWorkflowSession() {
			return workflowSession;
		}
		
	}

}
