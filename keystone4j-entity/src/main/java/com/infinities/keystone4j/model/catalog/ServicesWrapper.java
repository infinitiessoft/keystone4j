package com.infinities.keystone4j.model.catalog;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class ServicesWrapper implements CollectionWrapper<Service> {

	private List<Service> services;
	private boolean truncated;

	private Links links = new Links();


	public ServicesWrapper() {

	}

	public ServicesWrapper(List<Service> services) {
		this.services = services;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
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
	public void setRefs(List<Service> refs) {
		this.services = refs;
	}

}
