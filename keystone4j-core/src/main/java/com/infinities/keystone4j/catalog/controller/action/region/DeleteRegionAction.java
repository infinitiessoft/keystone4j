package com.infinities.keystone4j.catalog.controller.action.region;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteRegionAction extends AbstractRegionAction implements ProtectedAction<Region> {

	private final String regionid;


	public DeleteRegionAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String regionid) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.regionid = regionid;
	}

	@Override
	public MemberWrapper<Region> execute(ContainerRequestContext context) throws Exception {
		this.getCatalogApi().deleteRegion(regionid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_region";
	}
}
