package com.infinities.keystone4j.trust.model;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class TrustWrapper {

	private Trust trust;


	public TrustWrapper() {

	}

	public TrustWrapper(Trust trust, ContainerRequestContext context) {
		this(trust, context.getUriInfo().getBaseUri().toASCIIString() + "v3/trusts/");
	}

	public TrustWrapper(Trust trust, String baseUrl) {
		this.trust = trust;
		ReferentialLinkUtils.instance.addSelfReferentialLink(trust, baseUrl);
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

}
