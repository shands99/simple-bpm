package org.jemco.simplebpm.runtime;

import java.util.List;

import org.jemco.simplebpm.action.ActionHandler;

public interface ActionState extends State {

	List<ActionHandler> getActionHandlers();
	
	void addEntryHandler(ActionHandler handler);
	
	void addExitHandler(ActionHandler handler);
	
}
