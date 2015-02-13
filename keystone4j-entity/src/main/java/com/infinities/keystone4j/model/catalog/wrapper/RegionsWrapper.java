package com.infinities.keystone4j.model.catalog.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.common.CollectionLinks;

public class RegionsWrapper implements CollectionWrapper<Region> {

	private List<Region> regions;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public RegionsWrapper() {

	}

	public RegionsWrapper(List<Region> regions) {
		this.regions = regions;
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
	public void setRefs(List<Region> refs) {
		this.regions = refs;
	}

	@XmlElement(name = "regions")
	public List<Region> getRefs() {
		return regions;
	}

}
