package org.jemco.simplebpm.runtime;

import org.jemco.simplebpm.utils.Holder;

public abstract class BaseValidatingEntity implements ValidatingEntity {

	abstract protected boolean doValidate(Holder holder);
	
	@Override
	public boolean validate() {
		return validate(new Holder<Exception>());
	}

	@Override
	public boolean validate(Holder holder) {
		try {
			return this.doValidate(holder);
		} catch (Exception e) {
			if (holder != null) {
				holder.setContainer(e);
			}
		}
		return false;
	}
	
}
