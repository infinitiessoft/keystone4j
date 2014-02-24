package com.infinities.keystone4j.trust.model;

public class SignedWrapper {

	String signed;


	public SignedWrapper() {

	}

	public SignedWrapper(String signed) {
		super();
		this.signed = signed;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

}
