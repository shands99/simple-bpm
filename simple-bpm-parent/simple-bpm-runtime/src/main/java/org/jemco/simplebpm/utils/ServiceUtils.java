package org.jemco.simplebpm.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServiceUtils {
	
	private ServiceUtils(){}
	
	private static final String MSG_TOO_MANY_SVC = "More than one SPI service found on the classpath for type {0}. Using default implementation.";
	
	private static final String MSG_NO_SVC = "No SPI service found on the classpath for type {0}. Using default implementation.";
	
	private static final Logger LOG = LoggerFactory.getLogger(ServiceUtils.class);
	
	public static <T> T load(Class<T> type) {
		
		List<T> results = new ArrayList<T>();
		ServiceLoader<T> loader = ServiceLoader.load(type);
		Iterator<T> iterator = loader.iterator();
		while(iterator.hasNext()) {
			results.add(iterator.next());
		}
		
		if (results.size() > 1) {
			LOG.error(MSG_TOO_MANY_SVC, type);
			return null;
		} else if (results.size() == 0) {
			LOG.error(MSG_NO_SVC, type);
		}
		
		return results.get(0);
		
	}
	
}
