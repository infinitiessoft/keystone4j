package com.infinities.keystone4j.catalog.model;

import java.util.List;

public class ServicesWrapper {

	private List<Service> services;


	public ServicesWrapper(List<Service> services) {
		super();
		this.services = services;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

}
