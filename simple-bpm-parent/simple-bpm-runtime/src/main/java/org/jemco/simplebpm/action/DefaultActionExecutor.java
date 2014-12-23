package org.jemco.simplebpm.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jemco.simplebpm.WorkflowSession;
import org.jemco.simplebpm.runtime.ActionExecutorRole;

public class DefaultActionExecutor implements ActionExecutor {

	@Override
	public void executeActions(ActionExecutorRole state,
			WorkflowSession session, Phase phase) throws ActionException {
		
		// execute any actions in order of salience
		switch(phase) {
		case IN :
			executeActions(state.getEntryHandlers(), session);
			break;
		case OUT :
			executeActions(state.getExitHandlers(), session);
			break;
		}
		
	}
	
	protected void executeActions(List<ActionHandler> actionHandlers, WorkflowSession session) throws ActionException {
		
		Collections.sort(actionHandlers, new Comparator<ActionHandler>(){
			@Override
			public int compare(ActionHandler o1, ActionHandler o2) {
				return o1.getSalience() - o2.getSalience();
			}});
		
		for (ActionHandler handler : actionHandlers) {
			handler.execute(session);
		}
		
	}

}
