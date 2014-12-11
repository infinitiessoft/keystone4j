package com.infinities.keystone4j.model.catalog;

import javax.persistence.Transient;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.common.Links;

public class CatalogWrapper implements MemberWrapper<Catalog> {

	private Catalog catalog;
	private Links links = new Links();


	public CatalogWrapper() {

	}

	public CatalogWrapper(Catalog catalog) {
		this.catalog = catalog;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(catalog,
		// baseUrl);
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	@Override
	public void setRef(Catalog ref) {
		this.catalog = ref;
	}

	@Transient
	public Links getLinks() {
		return links;
	}

	@Transient
	public void setLinks(Links links) {
		this.links = links;
	}
}
