package com.infinities.keystone4j.model.policy.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.policy.Policy;

public class PoliciesWrapper implements CollectionWrapper<Policy> {

	private List<Policy> policies;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public PoliciesWrapper() {

	}

	public PoliciesWrapper(List<Policy> policies) {
		this.policies = policies;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
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
	public void setRefs(List<Policy> refs) {
		this.policies = refs;
	}

	@XmlElement(name = "policies")
	public List<Policy> getRefs() {
		return policies;
	}

}
