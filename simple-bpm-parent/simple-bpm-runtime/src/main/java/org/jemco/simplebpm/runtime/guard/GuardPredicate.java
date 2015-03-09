package org.jemco.simplebpm.runtime.guard;

import org.jemco.simplebpm.runtime.Context;

public interface GuardPredicate {

	boolean accept(Context context);
	
}
