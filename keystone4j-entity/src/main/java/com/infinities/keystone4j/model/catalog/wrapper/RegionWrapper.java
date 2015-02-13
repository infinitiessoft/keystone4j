package com.infinities.keystone4j.model.catalog.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;

public class RegionWrapper implements MemberWrapper<Region> {

	private Region region;


	public RegionWrapper() {

	}

	public RegionWrapper(Region region) {
		this.region = region;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(region,
		// baseUrl);
	}

	@Override
	public void setRef(Region ref) {
		this.region = ref;
	}

	@XmlElement(name = "region")
	@Override
	public Region getRef() {
		return region;
	}
}
