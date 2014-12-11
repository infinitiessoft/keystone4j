package com.infinities.keystone4j.model.catalog;

import com.infinities.keystone4j.model.MemberWrapper;

public class ServiceWrapper implements MemberWrapper<Service> {

	private Service service;


	public ServiceWrapper() {

	}

	public ServiceWrapper(Service service) {
		this.service = service;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(service,
		// baseUrl);
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	@Override
	public void setRef(Service ref) {
		this.service = ref;
	}
}
