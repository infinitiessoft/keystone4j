package com.infinities.keystone4j.catalog.model;

import java.util.List;

public class EndpointsWrapper {

	private List<Endpoint> endpoints;


	public EndpointsWrapper(List<Endpoint> endpoints) {
		super();
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

}
