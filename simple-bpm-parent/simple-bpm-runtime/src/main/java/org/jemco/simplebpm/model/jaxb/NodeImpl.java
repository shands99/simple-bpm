package org.jemco.simplebpm.model.jaxb;

import javax.xml.bind.annotation.XmlType;

import org.jemco.simplebpm.model.Node;

public class NodeImpl implements Node {

	private String type;
	
	private String name;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
