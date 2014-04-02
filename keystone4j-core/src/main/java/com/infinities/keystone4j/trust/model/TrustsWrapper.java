package com.infinities.keystone4j.trust.model;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.common.model.Links;

public class TrustsWrapper {

	private List<Trust> trusts;
	private Links links = new Links();


	public TrustsWrapper(List<Trust> trusts, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/trusts/";
		this.trusts = trusts;
		for (Trust trust : trusts) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(trust, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Trust> getTrusts() {
		return trusts;
	}

	public void setTrusts(List<Trust> trusts) {
		this.trusts = trusts;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
