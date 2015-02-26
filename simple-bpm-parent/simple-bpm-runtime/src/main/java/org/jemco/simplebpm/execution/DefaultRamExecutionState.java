package org.jemco.simplebpm.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jemco.simplebpm.process.State;
import org.jemco.simplebpm.process.StateTransition;

public class DefaultRamExecutionState implements ExecutionState {

	private String id;
	
	private ArrayList<State> stateHistory = new ArrayList<State>();
	
	private State currentState;
	
	private State start;
	
	private Object monitor = new Object();
	
	private ExecutionState parent;
	
	private List<ExecutionState> children = new ArrayList<ExecutionState>();
	
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
			return stateHistory.size() > 0 ? stateHistory.get((stateHistory.size() - 1)) : null;
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

	@Override
	public ExecutionState getParent() {
		return parent;
	}

	@Override
	public List<ExecutionState> getChildren() {
		return children;
	}

	public void setParent(ExecutionState parent) {
		this.parent = parent;
	}

}
