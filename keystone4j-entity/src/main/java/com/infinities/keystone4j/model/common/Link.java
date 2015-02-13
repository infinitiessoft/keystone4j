package com.infinities.keystone4j.model.common;

public class Link implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3037390869002692024L;
	private String href;
	private String rel;
	private String type;


	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
