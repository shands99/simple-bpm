package org.jemco.simplebpm.utils;

import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Loader {

	private static final Logger log = LoggerFactory.getLogger(Loader.class);
	
	private static final String MORE_THAN_ONE_SVC = "More than one service found on the classpath for type {0}.";
	
	public static <T> T load(Class<T> type) {
		
		ServiceLoader<T> loader = ServiceLoader.load(type, type.getClassLoader());
		
		int i = 0;
		T ret = null;
		while (loader.iterator().hasNext()) {
			if (i == 0) {
				ret = loader.iterator().next();
			} else if(i > 1) {
				log.error(MORE_THAN_ONE_SVC, type.getName());
			}
			i++;
		}
		
		return ret;
		
	}
	
}
