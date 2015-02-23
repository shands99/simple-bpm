package org.jemco.simplebpm.runtime;

public class SubProcessRoleImpl implements SubProcessRole {

	private State parent;
		
	private Process subProcess;

	public SubProcessRoleImpl(State parent, Process subProcess) {
		super();
		this.parent = parent;
		this.subProcess = subProcess;
	}
	
	public State getParent() {
		return parent;
	}

	public Process getSubProcess() {
		return subProcess;
	}
	
}
