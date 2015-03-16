package org.jemco.simplebpm.process.loader;

import org.jemco.simplebpm.io.Resource;
import org.jemco.simplebpm.process.Process;

public interface ProcessLoader {

	Process createProcess(Resource resource);
	
}
