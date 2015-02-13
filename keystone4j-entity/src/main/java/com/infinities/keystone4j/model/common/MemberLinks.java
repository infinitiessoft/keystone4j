package com.infinities.keystone4j.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MemberLinks implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String self;


	@JsonInclude(Include.ALWAYS)
	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

}
