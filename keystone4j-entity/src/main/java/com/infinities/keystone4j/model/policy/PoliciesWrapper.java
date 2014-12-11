package com.infinities.keystone4j.model.policy;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class PoliciesWrapper implements CollectionWrapper<Policy> {

	private List<Policy> policies;
	private boolean truncated;

	private Links links = new Links();


	public PoliciesWrapper() {

	}

	public PoliciesWrapper(List<Policy> policies) {
		this.policies = policies;
	}

	public List<Policy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
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
	public void setRefs(List<Policy> refs) {
		this.policies = refs;
	}

}
