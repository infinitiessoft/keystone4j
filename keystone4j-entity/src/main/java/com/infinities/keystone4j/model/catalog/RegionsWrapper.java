package com.infinities.keystone4j.model.catalog;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class RegionsWrapper implements CollectionWrapper<Region> {

	private List<Region> regions;
	private boolean truncated;

	private Links links = new Links();


	public RegionsWrapper() {

	}

	public RegionsWrapper(List<Region> regions) {
		this.regions = regions;
	}

	public List<Region> getRegions() {
		return regions;
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
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
	public void setRefs(List<Region> refs) {
		this.regions = refs;
	}

}
