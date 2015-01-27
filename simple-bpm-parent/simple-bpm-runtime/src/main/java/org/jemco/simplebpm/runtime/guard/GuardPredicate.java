package org.jemco.simplebpm.runtime.guard;

import org.jemco.simplebpm.runtime.execution.Context;

public interface GuardPredicate {

	boolean accept(Context context);
	
}
