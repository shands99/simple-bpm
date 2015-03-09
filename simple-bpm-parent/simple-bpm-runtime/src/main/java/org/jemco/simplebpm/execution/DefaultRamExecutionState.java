package org.jemco.simplebpm.execution;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jemco.simplebpm.process.State;

public class DefaultRamExecutionState implements ExecutionState {

	private String id;
		
	private List<Token> tokenHistory = new LinkedList<Token>();
		
	private Object monitor = new Object();
	
	private ExecutionState parent;
	
	private List<ExecutionState> children = new ArrayList<ExecutionState>();
	
	public DefaultRamExecutionState(State start) 
	{
		this.id = UUID.randomUUID().toString();
		createToken(start.getName(), false, false);
	}
	
	public DefaultRamExecutionState(String id, State start) 
	{
		this.id = id;
		createToken(start.getName(), false, false);
	}
	
	@Override
	public void createToken(String name, boolean blocking, boolean end) {
		synchronized(monitor) {
			
			Token previous = getPreviousToken(true, false);
			if (previous != null) {
				previous.setActive(false);
				previous.setComplete(true);
			}
			
			Token token = new Token();
			token.setActive(true);
			token.setBlocking(blocking);
			token.setEnd(end);
			token.setName(name);
			tokenHistory.add(token);
			
		}
	}

	public String getId() {
		return id;
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

	
	
	@SuppressWarnings("unchecked")
	@Override
	public Token getCurrentToken() {
		synchronized(monitor) {
			return ((Deque<Token>)tokenHistory).getLast();
		}
	}

	@Override
	public Token getPreviousToken() {
		return getPreviousToken(false, true);
	}

	private Token getPreviousToken(boolean active, boolean complete) {
		synchronized(monitor) {
			Iterator<Token> descendingIterator = ((LinkedList)tokenHistory).descendingIterator();
			while(descendingIterator.hasNext()) {
				Token token = descendingIterator.next();
				if (token.isActive() == active && token.isComplete() == complete) {
					return token;
				}
			}
			return null;
		}
	}


}
