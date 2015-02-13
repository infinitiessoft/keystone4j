package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;

public interface RegionV3Controller {

	MemberWrapper<Region> createRegion(Region region) throws Exception;

	CollectionWrapper<Region> listRegions() throws Exception;

	MemberWrapper<Region> getRegion(String regionid) throws Exception;

	MemberWrapper<Region> updateRegion(String regionid, Region region) throws Exception;

	void deleteRegion(String regionid) throws Exception;

	MemberWrapper<Region> createRegionWithId(String regionid, Region region) throws Exception;

}
