package org.jemco.simplebpm.event.copy;

public abstract class EventServiceFactory {

	private EventServiceFactory INSTANCE;
	
	public static EventServiceFactory getFactory() {
		return null;
	}
	
	public EventService getEventService() {
		return null;
	}
	
}
