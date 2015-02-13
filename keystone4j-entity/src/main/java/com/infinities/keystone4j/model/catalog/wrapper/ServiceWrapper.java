package com.infinities.keystone4j.model.catalog.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;

public class ServiceWrapper implements MemberWrapper<Service> {

	private Service service;


	public ServiceWrapper() {

	}

	public ServiceWrapper(Service service) {
		this.service = service;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(service,
		// baseUrl);
	}

	@Override
	public void setRef(Service ref) {
		this.service = ref;
	}

	@XmlElement(name = "service")
	@Override
	public Service getRef() {
		return service;
	}
}
