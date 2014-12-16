package com.infinities.keystone4j.catalog.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.RegionV3Controller;
import com.infinities.keystone4j.catalog.controller.action.region.CreateRegionAction;
import com.infinities.keystone4j.catalog.controller.action.region.DeleteRegionAction;
import com.infinities.keystone4j.catalog.controller.action.region.GetRegionAction;
import com.infinities.keystone4j.catalog.controller.action.region.ListRegionsAction;
import com.infinities.keystone4j.catalog.controller.action.region.UpdateRegionAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.catalog.controllers.RegionV3 20141216

public class RegionV3ControllerImpl extends BaseController implements RegionV3Controller {

	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public RegionV3ControllerImpl(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<Region> createRegion(Region region) throws Exception {
		ProtectedAction<Region> command = new ProtectedDecorator<Region>(new CreateRegionAction(catalogApi,
				tokenProviderApi, policyApi, region), tokenProviderApi, policyApi);
		MemberWrapper<Region> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Region> listRegions() throws Exception {
		FilterProtectedAction<Region> command = new FilterProtectedDecorator<Region>(new ListRegionsAction(catalogApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Region> ret = command.execute(getRequest(), "parent_region_id");
		return ret;
	}

	@Override
	public MemberWrapper<Region> getRegion(String regionid) {
		Region ref = getMemberFromDriver(regionid);
		ProtectedAction<Region> command = new ProtectedDecorator<Region>(new GetRegionAction(catalogApi, tokenProviderApi,
				policyApi, regionid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Region> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Region> updateRegion(String regionid, Region region) {
		Region ref = getMemberFromDriver(regionid);
		ProtectedAction<Region> command = new ProtectedDecorator<Region>(new UpdateRegionAction(catalogApi,
				tokenProviderApi, policyApi, regionid, region), tokenProviderApi, policyApi, ref);
		MemberWrapper<Region> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteRegion(String regionid) {
		Region ref = getMemberFromDriver(regionid);
		ProtectedAction<Region> command = new ProtectedDecorator<Region>(new DeleteRegionAction(catalogApi,
				tokenProviderApi, policyApi, regionid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	public Region getMemberFromDriver(String regionid) {
		return catalogApi.getRegion(regionid);
	}

}
