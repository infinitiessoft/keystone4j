package com.infinities.keystone4j.model.trust;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class TrustsWrapper implements CollectionWrapper<Trust> {

	private List<Trust> trusts;
	private boolean truncated;

	private Links links = new Links();


	public TrustsWrapper() {

	}

	public TrustsWrapper(List<Trust> trusts) {
		this.trusts = trusts;
	}

	public List<Trust> getTrusts() {
		return trusts;
	}

	public void setTrusts(List<Trust> trusts) {
		this.trusts = trusts;
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
	public void setRefs(List<Trust> refs) {
		this.trusts = refs;
	}

}
