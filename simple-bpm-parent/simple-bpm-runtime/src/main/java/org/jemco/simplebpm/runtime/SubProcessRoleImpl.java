package org.jemco.simplebpm.runtime;

public class SubProcessRoleImpl implements SubProcessRole {

	private State parent;
	
	private String processName;

	public SubProcessRoleImpl(State parent, String processName) {
		super();
		this.parent = parent;
		this.processName = processName;
	}
	
	public State getParent() {
		return parent;
	}

	public String getProcessName() {
		return processName;
	}
	
}
