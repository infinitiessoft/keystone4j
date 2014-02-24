package com.infinities.keystone4j.catalog.model;

public class EndpointWrapper {

	private Endpoint endpoint;


	public EndpointWrapper() {

	}

	public EndpointWrapper(Endpoint endpoint) {
		super();
		this.endpoint = endpoint;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

}
