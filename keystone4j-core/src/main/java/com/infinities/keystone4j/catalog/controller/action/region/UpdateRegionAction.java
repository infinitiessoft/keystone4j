package com.infinities.keystone4j.catalog.controller.action.region;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateRegionAction extends AbstractRegionAction implements ProtectedAction<Region> {

	private final String regionid;
	private final Region region;


	public UpdateRegionAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String regionid, Region region) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.regionid = regionid;
		this.region = region;
	}

	@Override
	public MemberWrapper<Region> execute(ContainerRequestContext context) throws Exception {
		requireMatchingId(regionid, region);
		Region ref = this.getCatalogApi().updateRegion(regionid, region);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_region";
	}
}
