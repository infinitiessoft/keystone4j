package com.infinities.keystone4j.model.assignment;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class DomainsWrapper implements CollectionWrapper<Domain> {

	private List<Domain> domains;
	private boolean truncated;

	private Links links = new Links();


	public DomainsWrapper() {

	}

	public DomainsWrapper(List<Domain> domains) {
		this.domains = domains;
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
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
	public void setRefs(List<Domain> refs) {
		this.domains = refs;
	}

}
