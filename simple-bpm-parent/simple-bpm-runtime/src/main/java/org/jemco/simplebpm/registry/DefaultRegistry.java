package org.jemco.simplebpm.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;

public class DefaultRegistry implements Registry {

	private Map<String, Object> registryObjects = new ConcurrentHashMap<String, Object>();
	
	@Override
	public void add(Object object) {
		registryObjects.put(object.getClass().getSimpleName(), object);
	}

	@Override
	public void add(String classifier, Object object) {
		registryObjects.put(classifier, object);
	}

	@Override
	public <T> T get(final Class<T> clazz) {
		
		List<Object> values = FunctionUtils.predicateCollection(new ArrayList<Object>(registryObjects.values()), new Predicate<Object>(){
			@Override
			public boolean accept(Object object) {
				boolean matches = object.getClass().equals(clazz);
				if (!matches) {
					matches = clazz.isAssignableFrom(object.getClass());
				}
				return matches;
			}});
		
		return (T) (values.size() == 1 ? values.get(0) : null);
		
	}

	@Override
	public <T> T get(String classifier) {
		return (T) registryObjects.get(classifier);
	}

	@Override
	public <T> T get(String classifier, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
