package org.jemco.simplebpm.runtime.execution;

import java.util.ArrayList;
import java.util.UUID;

import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateTransition;

public class RamExecutionContext implements ExecutionContext {

	private String id;
	
	private ArrayList<State> stateHistory = new ArrayList<State>();
	
	private State currentState;
	
	private State start;
	
	public RamExecutionContext(State start) 
	{
		this.id = UUID.randomUUID().toString();
		this.currentState = start;
	}
	
	@Override
	public State executeTransition(StateTransition transition) {
		
		stateHistory.add(this.currentState);
		this.currentState = transition.getTargetState();
		
		return transition.getTargetState();
	}

	@Override
	public State getPrevious() {
		return stateHistory.get((stateHistory.size() - 1));
	}

	public String getId() {
		return id;
	}

	public State getCurrentState() {
		return currentState;
	}

	public State getStart() {
		return start;
	}

}
