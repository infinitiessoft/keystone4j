package com.infinities.keystone4j.model.assignment.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class DomainsWrapper implements CollectionWrapper<Domain> {

	private List<Domain> domains;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public DomainsWrapper() {

	}

	public DomainsWrapper(List<Domain> domains) {
		this.domains = domains;
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
	public void setRefs(List<Domain> refs) {
		this.domains = refs;
	}

	@XmlElement(name = "domains")
	public List<Domain> getRefs() {
		return domains;
	}

}
