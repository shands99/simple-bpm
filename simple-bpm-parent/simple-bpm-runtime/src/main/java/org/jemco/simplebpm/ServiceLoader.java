package org.jemco.simplebpm;

import java.util.List;

public interface ServiceLoader {

	void load() throws WorkflowException;
	List<Object> getServices();
}
