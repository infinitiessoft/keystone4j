package com.infinities.keystone4j.credential.model;

import java.util.List;

public class CredentialsWrapper {

	private List<Credential> credentials;


	public CredentialsWrapper(List<Credential> credentials) {
		super();
		this.credentials = credentials;
	}

	public List<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(List<Credential> credentials) {
		this.credentials = credentials;
	}

}
