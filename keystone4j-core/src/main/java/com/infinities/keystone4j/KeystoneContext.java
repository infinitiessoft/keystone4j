package com.infinities.keystone4j;

import javax.xml.bind.annotation.XmlElement;

public class KeystoneContext {

	public final static String CONTEXT_NAME = "openstack.context";

	private boolean isAdmin = false;
	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "subject_token_id")
	private String subjectTokenid;
	private Environment environment;


	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public String getSubjectTokenid() {
		return subjectTokenid;
	}

	public void setSubjectTokenid(String subjectTokenid) {
		this.subjectTokenid = subjectTokenid;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
