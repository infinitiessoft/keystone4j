package com.infinities.keystone4j.model.catalog;

import com.infinities.keystone4j.model.MemberWrapper;

public class EndpointWrapper implements MemberWrapper<Endpoint> {

	private Endpoint endpoint;


	public EndpointWrapper() {

	}

	public EndpointWrapper(Endpoint endpoint) {
		this.endpoint = endpoint;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(endpoint,
		// baseUrl);
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public void setRef(Endpoint ref) {
		this.endpoint = ref;
	}
}
