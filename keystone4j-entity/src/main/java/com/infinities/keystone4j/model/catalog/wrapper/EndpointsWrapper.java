package com.infinities.keystone4j.model.catalog.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class EndpointsWrapper implements CollectionWrapper<Endpoint> {

	private List<Endpoint> endpoints;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public EndpointsWrapper() {

	}

	public EndpointsWrapper(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
		this.links = links;
	}

	@JsonView(Views.All.class)
	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	@XmlElement(name = "endpoints")
	public void setRefs(List<Endpoint> refs) {
		this.endpoints = refs;
	}

	@XmlElement(name = "endpoints")
	public List<Endpoint> getRefs() {
		return endpoints;
	}

}
