package org.jemco.simplebpm.registry;

import org.springframework.context.ApplicationContext;

public class SpringRegistry implements Registry {

	private ApplicationContext applicationContext;
	
	public SpringRegistry(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}
	
	public void add(Object object) {
		
	}

	public void add(String classifier, Object object) {
		// TODO Auto-generated method stub
		
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
