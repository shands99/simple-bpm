package org.jemco.simplebpm.model;

import java.util.List;

public interface Process {

	String getName();
	
	Node getStart();
	
	List<Node> getNodes();
	
	List<Transition> getTransitions();
	
	List<Node> getEndNodes();
	
	String getExecutionContextType();
	
}
