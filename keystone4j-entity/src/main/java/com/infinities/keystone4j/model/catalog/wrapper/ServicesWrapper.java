package com.infinities.keystone4j.model.catalog.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class ServicesWrapper implements CollectionWrapper<Service> {

	private List<Service> services;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public ServicesWrapper() {

	}

	public ServicesWrapper(List<Service> services) {
		this.services = services;
	}

	@JsonView(Views.Advance.class)
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
	public void setRefs(List<Service> refs) {
		this.services = refs;
	}

	@JsonView(Views.Advance.class)
	@XmlElement(name = "services")
	public List<Service> getRefs() {
		return services;
	}
}
