package org.jemco.simplebpm.runtime.guard;

import org.jemco.simplebpm.execution.Context;

public interface GuardPredicate {

	boolean accept(Context context);
	
}
