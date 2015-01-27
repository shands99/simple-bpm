package org.jemco.simplebpm.event;

public interface EventHandler {

	boolean accept(Event event);
	void listen(Event event);
	
}
