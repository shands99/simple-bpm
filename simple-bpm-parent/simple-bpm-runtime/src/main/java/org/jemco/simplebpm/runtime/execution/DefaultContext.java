package org.jemco.simplebpm.runtime.execution;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.jemco.simplebpm.registry.Registry;

public class DefaultContext implements Context {

	private Registry registry;
	
	private Map<String, Object> contextVariables = new HashMap<String, Object>();

	public DefaultContext(Registry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public void addContextVariable(String key, Object var) {
		
		if (this.contextVariables.containsKey(key)) {
			throw new IllegalArgumentException(MessageFormat.format("Object already exists in context for key {0}.", key));
		}
		
		contextVariables.put(key, var);
		
	}

	@Override
	public Object getContextVar(String key) {
		Object var = contextVariables.get(key);
		if (var == null)
			var = this.registry.get(key);
		
		return var;
	}

	@Override
	public <T> T getContextVar(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
