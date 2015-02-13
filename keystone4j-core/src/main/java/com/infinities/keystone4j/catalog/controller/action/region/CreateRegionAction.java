package com.infinities.keystone4j.catalog.controller.action.region;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateRegionAction extends AbstractRegionAction implements ProtectedAction<Region> {

	private final Region region;


	public CreateRegionAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, Region region) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.region = region;
	}

	@Override
	public MemberWrapper<Region> execute(ContainerRequestContext context) throws Exception {
		if (!Strings.isNullOrEmpty(region.getId())) {
			assignUniqueId(region);
		}

		Region ref = catalogApi.createRegion(region);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_region";
	}
}
