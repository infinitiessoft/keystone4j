package com.infinities.keystone4j.credential.model;


public class CredentialWrapper {

	private Credential credential;


	public CredentialWrapper() {

	}

	public CredentialWrapper(Credential credential) {
		super();
		this.credential = credential;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

}
