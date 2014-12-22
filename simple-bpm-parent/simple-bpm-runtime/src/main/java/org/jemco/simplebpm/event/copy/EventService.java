package org.jemco.simplebpm.event.copy;

public interface EventService {

	void addHandler(EventHandler handler);
	
	void raiseEvent(Event event);
		
}
