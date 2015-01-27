package org.jemco.simplebpm.runtime.execution;

import java.util.ArrayList;
import java.util.UUID;

import org.jemco.simplebpm.runtime.State;
import org.jemco.simplebpm.runtime.StateTransition;

public class DefaultRamExecutionState implements ExecutionState {

	private String id;
	
	private ArrayList<State> stateHistory = new ArrayList<State>();
	
	private State currentState;
	
	private State start;
	
	private Object monitor = new Object();
	
	public DefaultRamExecutionState(State start) 
	{
		this.id = UUID.randomUUID().toString();
		this.currentState = start;
	}
	
	public DefaultRamExecutionState(String id, State start) 
	{
		this.id = id;
		this.currentState = start;
	}
	
	@Override
	public State executeTransition(StateTransition transition) {
		
		synchronized(monitor) {
			stateHistory.add(this.currentState);
			this.currentState = transition.getTargetState();
		}
		
		return transition.getTargetState();
	}

	@Override
	public State getPrevious() {
		synchronized (monitor) {
			return stateHistory.get((stateHistory.size() - 1));
		}
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
