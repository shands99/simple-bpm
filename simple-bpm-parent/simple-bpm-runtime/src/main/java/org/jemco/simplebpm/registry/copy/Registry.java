package org.jemco.simplebpm.registry.copy;

public interface Registry {

	void add(Object object);
	
	void add(String classifier, Object object);
	
	<T> T get(Class<T> clazz);
	
	<T> T get(String classifier, Class<T> clazz);
	
}
