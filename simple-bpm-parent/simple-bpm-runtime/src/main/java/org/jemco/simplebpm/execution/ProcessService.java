package org.jemco.simplebpm.execution;

import org.jemco.simplebpm.process.Process;

public interface ProcessService {

	Process save(String name);
	
	Process retrieve(String name);
	
}
