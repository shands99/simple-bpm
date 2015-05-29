package org.jemco.simplebpm;

import java.util.List;

public interface ServiceLoader {

	void load();
	List<Object> getServices();
}
