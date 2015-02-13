package com.infinities.keystone4j.model.catalog.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class EndpointWrapper implements MemberWrapper<Endpoint> {

	private Endpoint endpoint;


	public EndpointWrapper() {

	}

	public EndpointWrapper(Endpoint endpoint) {
		this.endpoint = endpoint;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(endpoint,
		// baseUrl);
	}

	@Override
	public void setRef(Endpoint ref) {
		this.endpoint = ref;
	}

	@XmlElement(name = "endpoint")
	@Override
	public Endpoint getRef() {
		return endpoint;
	}
}
