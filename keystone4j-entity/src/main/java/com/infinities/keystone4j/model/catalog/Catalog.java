package com.infinities.keystone4j.model.catalog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Catalog {

	private String id;
	private String type;
	private Set<Endpoint> endpoints = new HashSet<Endpoint>();
	private List<Service> services = new ArrayList<Service>();


	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setEndpoints(Set<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public Set<Endpoint> getEndpoints() {
		return endpoints;
	}

}
