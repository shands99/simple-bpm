package org.jemco.simplebpm;

import org.jemco.simplebpm.registry.SpringRegistryWrapper;
import org.jemco.simplebpm.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SimpleBpmSpringApplication extends SimpleBpmApplication implements ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;
	
	public SimpleBpmSpringApplication() {}
	
	public SimpleBpmSpringApplication(ApplicationContext applicationContext) {
		super();
		this.applicationContext = applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected void loadRegistry() {
		
		Assert.notNull(applicationContext, "Spring context is null.");
		registry = new SpringRegistryWrapper(applicationContext);
		
	}

	public void afterPropertiesSet() throws Exception {
		start();
	}

}
