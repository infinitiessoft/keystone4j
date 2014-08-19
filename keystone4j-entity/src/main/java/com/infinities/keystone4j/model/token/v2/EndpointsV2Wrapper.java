package com.infinities.keystone4j.model.token.v2;

import java.util.ArrayList;
import java.util.List;

import com.infinities.keystone4j.model.common.Link;

public class EndpointsV2Wrapper {

	private List<Access.Service> endpoints;
	private final List<Link> endpointsLinks = new ArrayList<Link>();


	public List<Access.Service> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Access.Service> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Link> getEndpointsLinks() {
		return endpointsLinks;
	}

}
