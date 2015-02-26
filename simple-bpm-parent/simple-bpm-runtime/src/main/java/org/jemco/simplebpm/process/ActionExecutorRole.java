package org.jemco.simplebpm.process;

import java.util.List;

import org.jemco.simplebpm.action.ActionHandler;

public interface ActionExecutorRole extends StateRole  {

	List<ActionHandler> getEntryHandlers();
	
	List<ActionHandler> getExitHandlers();
	
	void addEntryHandler(ActionHandler handler);
	
	void addExitHandler(ActionHandler handler);
	
}
