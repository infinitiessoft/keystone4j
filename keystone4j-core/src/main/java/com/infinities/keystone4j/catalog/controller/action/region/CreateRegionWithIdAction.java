package com.infinities.keystone4j.catalog.controller.action.region;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateRegionWithIdAction extends AbstractRegionAction implements ProtectedAction<Region> {

	private final String regionid;
	private final Region region;


	public CreateRegionWithIdAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String regionid, Region region) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.regionid = regionid;
		this.region = region;
	}

	@Override
	public MemberWrapper<Region> execute(ContainerRequestContext context) throws Exception {
		if (!Strings.isNullOrEmpty(region.getId()) && !regionid.equals(region.getId())) {
			String msg = String.format("Conflicting region IDs specified: %s !- %s", regionid, region.getId());
			throw Exceptions.ValidationException.getInstance(msg);
		}
		region.setId(regionid);

		if (!Strings.isNullOrEmpty(region.getId())) {
			assignUniqueId(region);
		}

		Region ref = catalogApi.createRegion(region);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_region";
	}
}
