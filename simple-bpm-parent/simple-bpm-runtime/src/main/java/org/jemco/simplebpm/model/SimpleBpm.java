package org.jemco.simplebpm.model;

import java.util.List;

public interface SimpleBpm {

	List<EventListener> getEventListeners();
	
	Process getProcess();
	
}
