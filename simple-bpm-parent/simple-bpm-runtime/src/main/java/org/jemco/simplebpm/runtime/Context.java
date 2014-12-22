package org.jemco.simplebpm.runtime;

public interface Context {

	void addContextVariable(String key, Object var);
	
	Object getContextVar(String key);
	
	<T> T getContextVar(String key, Class<T> type);
	
	/**
	 * Retrieve by class type.  If more than one exists for the same type an exception is thrown.
	 * @param type
	 * @return
	 */
	<T> T getContextVar(Class<T> type);
	
	/**
	 * Return value from workflow / action execution.  Can be null.
	 * @return
	 */
	Object getReturnValue();
	
}
