package org.jemco.simplebpm.process;

import org.jemco.simplebpm.BaseRegistryService;
import org.jemco.simplebpm.io.Resource;
import org.jemco.simplebpm.process.loader.BpmnProcessLoader;
import org.jemco.simplebpm.process.loader.ProcessLoader;

/**
 * Simple implement that writes the process to the registry.  Note that the registry will almost certainly be writing to RAM and wil,l not be persistent 
 * (state not trackable) this should not be used in a production environment.  A more appropriate implementation would be a persistent store using JPA.<br/>
 * <br/>
 * Use for testing purposes only.
 * @author a583548
 *
 */
public class DefaultProcessService extends BaseRegistryService implements ProcessService {
	
	private ProcessLoader processLoader = new BpmnProcessLoader();

	@Override
	public void save(Process process) {
		
		registry.add(process.getName(), process);
		
	}
	
	@Override
	public Process retrieve(String name) {
		return registry.get(name);
	}

	@Override
	public Process create(String name, Resource resource) {
		
		Process process = retrieve(name);
		if (null == process) {
			process = processLoader.createProcess(resource);
			this.registry.add(name, process);
		}
		
		return process;
		
	}

	@Override
	public Process create(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
