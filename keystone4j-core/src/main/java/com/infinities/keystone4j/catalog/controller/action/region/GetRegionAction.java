package com.infinities.keystone4j.catalog.controller.action.region;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetRegionAction extends AbstractRegionAction implements ProtectedAction<Region> {

	private final String regionid;


	public GetRegionAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String regionid) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.regionid = regionid;
	}

	@Override
	public MemberWrapper<Region> execute(ContainerRequestContext context) {
		Region ref = this.getCatalogApi().getRegion(regionid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_region";
	}
}
