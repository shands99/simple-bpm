package org.jemco.simplebpm.process;

import org.jemco.simplebpm.io.Resource;


public interface ProcessService {
	
	void save(Process process);
	
	Process retrieve(String name);
	
	Process create(String name, Resource resource);
	
}
