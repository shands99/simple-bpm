package org.jemco.simplebpm.process;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.jemco.simplebpm.function.FunctionUtils;
import org.jemco.simplebpm.function.Predicate;
import org.jemco.simplebpm.utils.Assert;
import org.jemco.simplebpm.utils.Holder;

public class StateImpl extends BaseValidatingEntity implements State {

	private boolean end;
	
	private boolean blocking;
	
	private boolean start;
	
	private String name;
	
	private Process process;
			
	private List<StateTransition> exitTransitions = new ArrayList<StateTransition>();
	
	private List<StateRole> stateRoles = new ArrayList<StateRole>();
	
	StateImpl(boolean end, boolean blocking, String name) {
		this(end, blocking, name, null);
	}
	
	StateImpl(boolean end, boolean blocking, String name, Process process) {
		super();
		this.end = end;
		this.blocking = blocking;
		this.name = name;
		this.process = process;
	}
	
	@Override
	protected boolean doValidate(Holder holder) {
		
		Assert.notNull(this.process, "Process is null.");
		Assert.notNull(this.name, "name is null.");
		
		// check we only have one of each role
				
		// validate transitions
		for (StateTransition tr : this.exitTransitions) {
			if (!tr.validate(holder)) {
				return false;
			}
		}
		
		return true;
	}

	
	
	protected State addTransition(final String transitionName, String stateName) {
		
		StateTransition targetTransition = FunctionUtils.retrieveObjectFromCollection(exitTransitions, new Predicate<StateTransition>(){
			public boolean accept(StateTransition object) {
				return object.getName().equals(transitionName);
			}});
		
		if (targetTransition != null) {
			throw new IllegalArgumentException(MessageFormat.format("Transition {0} already exists in this process.", new Object[]{transitionName}));
		}
		
		// try and retrieve the target state by name - if it doesn;t exist create an outline version.
		State targetState = this.process.getState(stateName);
		if (targetState == null) {
			// create a simple outline state - all created states are not blocking. Override if nessacary.
			targetState = new StateImpl(false, false, stateName, this.process);
		}
		
		targetTransition = new StateTransitionImpl(transitionName, this, targetState);
		this.addTransition(targetTransition);
		return targetState;
		
	}
	
	protected void addTransition(StateTransition transition) {
		exitTransitions.add(transition);
	}
	
	@Override
	public String getName() {
		return name;
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

	@Override
	public void addStateRole(StateRole role) {
		// check if 1 exists already - you cannot add more than one of the same role
		StateRole existing = getRole(role.getClass());
		if (existing != null) {
			throw new IllegalArgumentException(MessageFormat.format("Role of type {0} already exists in state {1}", role.getClass().getName(), this.name));
		}
		this.stateRoles.add(role);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends StateRole> T getRole(final Class<T> roleType) {
		StateRole role = FunctionUtils.retrieveObjectFromCollection(this.stateRoles, new Predicate<StateRole>(){
			public boolean accept(StateRole object) {
				return roleType.isAssignableFrom(object.getClass());
			}});
		return (T) role;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	

	

}
