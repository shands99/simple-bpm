package org.jemco.simplebpm.registry;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

public class SpringRegistryWrapper implements Registry {

	private ApplicationContext applicationContext;
	
	public SpringRegistryWrapper(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}
	
	public void add(Object object) {
		if (get(object.getClass()) == null) {
			
			ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) applicationContext;
			beanFactory.registerSingleton(object.getClass().getSimpleName(), object);
			
		}
	}

	public void add(String classifier, Object object) {
		if (get(classifier, object.getClass()) == null) {
			
			ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) applicationContext;
			beanFactory.registerSingleton(classifier, object);
			
		}
	}

	public <T> T get(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public <T> T get(String classifier) {
		return (T) applicationContext.getBean(classifier);
	}

	public <T> T get(String classifier, Class<T> clazz) {
		return applicationContext.getBean(classifier, clazz);
	}



}
