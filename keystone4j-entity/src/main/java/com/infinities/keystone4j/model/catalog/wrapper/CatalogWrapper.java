package com.infinities.keystone4j.model.catalog.wrapper;

import java.util.List;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.common.CollectionLinks;

public class CatalogWrapper implements CollectionWrapper<Service> {

	private List<Service> catalog;
	private boolean truncated;
	private CollectionLinks links = new CollectionLinks();


	public CatalogWrapper() {

	}

	public CatalogWrapper(List<Service> catalog) {
		this.catalog = catalog;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(catalog,
		// baseUrl);
	}

	@Override
	@Transient
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	@Transient
	public void setLinks(CollectionLinks links) {
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
		this.catalog = refs;
	}

	@XmlElement(name = "catalog")
	public List<Service> getRefs() {
		return catalog;
	}
}
