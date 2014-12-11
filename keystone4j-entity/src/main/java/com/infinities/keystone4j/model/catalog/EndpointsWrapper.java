package com.infinities.keystone4j.model.catalog;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class EndpointsWrapper implements CollectionWrapper<Endpoint> {

	private List<Endpoint> endpoints;
	private boolean truncated;

	private Links links = new Links();


	public EndpointsWrapper() {

	}

	public EndpointsWrapper(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@Override
	public Links getLinks() {
		return links;
	}

	@Override
	public void setLinks(Links links) {
		this.links = links;
	}

	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Endpoint> refs) {
		this.endpoints = refs;
	}

}
