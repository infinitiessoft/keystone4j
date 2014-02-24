package com.infinities.keystone4j.identity.model;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;

public class UserParam extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = -158595822635574710L;
	@XmlAttribute(name = "original_password")
	@Transient
	private String originalPassword;


	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

}
