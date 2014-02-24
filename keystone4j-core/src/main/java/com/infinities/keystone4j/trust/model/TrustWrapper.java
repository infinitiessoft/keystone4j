package com.infinities.keystone4j.trust.model;

public class TrustWrapper {

	private Trust trust;


	public TrustWrapper() {

	}

	public TrustWrapper(Trust trust) {
		this.trust = trust;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

}
