package org.jemco.simplebpm.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateImpl implements State {

	private boolean end;
	private boolean blocking;
	private String name;
	
	private Map<String, StateTransition> transitions = new HashMap<String, StateTransition>();
		
	private List<StateTransition> exitTransitions = new ArrayList<StateTransition>();
	
	public StateImpl(boolean end, boolean blocking, String name) {
		super();
		this.end = end;
		this.blocking = blocking;
		this.name = name;
	}
	
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addTransition(StateTransition transition) {
		exitTransitions.add(transition);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public StateTransition getTransition(String name) {
		return transitions.get(name);
	}

	@Override
	public List<StateTransition> getExitTransitions() {
		return exitTransitions;
	}

	public boolean isEnd() {
		return end;
	}

	public boolean isBlocking() {
		return blocking;
	}

	

}
