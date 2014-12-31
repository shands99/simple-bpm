package org.jemco.simplebpm.utils;

public final class Assert {

	public static void notNull(Object object, String message) {
		if (object == null) {
			handleException(message);
		}
	}
	
	private static void handleException(String message) {
		if (message != null) {
			throw new IllegalArgumentException(message);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
}
