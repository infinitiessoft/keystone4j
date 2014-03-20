package com.infinities.keystone4j.identity.model;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;

public class UserParam {

	@XmlAttribute(name = "original_password")
	@Transient
	private String originalPassword;
	private String password;


	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
