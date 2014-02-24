package com.infinities.keystone4j.catalog.model;


public class ServiceWrapper {

	private Service service;


	public ServiceWrapper() {

	}

	public ServiceWrapper(Service service) {
		super();
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
