package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.utils.Holder;

public interface ValidatingEntity {

	boolean validate();
	
	boolean validate(Holder holder);
	
}
