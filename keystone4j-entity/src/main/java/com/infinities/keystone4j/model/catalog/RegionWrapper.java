package com.infinities.keystone4j.model.catalog;

import com.infinities.keystone4j.model.MemberWrapper;

public class RegionWrapper implements MemberWrapper<Region> {

	private Region region;


	public RegionWrapper() {

	}

	public RegionWrapper(Region region) {
		this.region = region;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(region,
		// baseUrl);
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	@Override
	public void setRef(Region ref) {
		this.region = ref;
	}
}
