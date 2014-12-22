package org.jemco.simplebpm.event.copy;

public abstract class BaseEvent implements Event {

	private String type;
	
	private String group;

	public BaseEvent(String type, String group) {
		super();
		this.type = type;
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public String getGroup() {
		return group;
	}
	
}
