package com.infinities.keystone4j.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CollectionLinks implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String self;
	private String next;
	private String previous;


	@JsonInclude(Include.ALWAYS)
	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	@JsonInclude(Include.ALWAYS)
	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	@JsonInclude(Include.ALWAYS)
	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

}
