package org.jemco.simplebpm.event;

public interface EventService {

	void addHandler(EventHandler handler);
	
	void raiseEvent(Event event);
		
}
