package org.jemco.simplebpm.execution;

import org.jemco.simplebpm.process.Process;

public class DefaultProcessManager implements ProcessService {

	private static final String SVC_MSG = "Not implemented in the default service.  You must create your own state management instance.";
	
	@Override
	public Process save(String name) {
		throw new UnsupportedOperationException(SVC_MSG);
	}

	@Override
	public Process retrieve(String name) {
		throw new UnsupportedOperationException(SVC_MSG);
	}
	
}
