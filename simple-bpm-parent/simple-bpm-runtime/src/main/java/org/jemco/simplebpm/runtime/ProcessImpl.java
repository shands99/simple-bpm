package org.jemco.simplebpm.runtime;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jemco.simplebpm.utils.Holder;

public class ProcessImpl extends BaseValidatingEntity implements Process {

	private Map<String, State> states = new HashMap<String, State>();
	
	private String name;
	
	public ProcessImpl(String name) {
		super();
		this.name = name;
	}
	
	@Override
	protected boolean doValidate(Holder holder) {
		
		// validate states
		for (Entry<String, State> stateEntry : this.states.entrySet()) {
			if (!stateEntry.getValue().validate(holder)) {
				return false;
			}
		}
				
		// must have at least 1 start & end
		
		// check the path through the process is correct
		
		return true;
	}
	
	public State addStartState(String name) {
		
		addState(name, false, false);
		State state = getState(name);
		if (state instanceof StateImpl) {
			((StateImpl) state).setStart(true);
		}
		return state;
		
	}
	
	public State addState(String name, boolean isBlocking, boolean isEnd) {
		
		if (this.states.containsKey(name)) {
			throw new IllegalArgumentException(MessageFormat.format("State {0} already exists", new Object[]{name}));
		}
		
		State state = new StateImpl(isEnd, isBlocking, name, this);
		states.put(name, state);
		
		return state;
		
	}
	
	public State addTransition(String sourceState, String transitionName, String targetState) {
		
		return this.addTransition(getState(sourceState), transitionName, targetState);
		
	}
	
	public State addTransition(State sourceState, String transitionName, String targetState) {
		
		if (sourceState == null) {
			throw new IllegalArgumentException("Source state is null.");
		}
		
		if (sourceState instanceof StateImpl) {
			State target = ((StateImpl) sourceState).addTransition(transitionName, targetState);
			if (!states.containsKey(target.getName())) {
				this.states.put(target.getName(),target);
			}
			return target;
		}
		
		return null;
		
	}
	
	@Override
	public State getState(String name) {
		return states.get(name);
	}

	public String getName() {
		return name;
	}

	

}
