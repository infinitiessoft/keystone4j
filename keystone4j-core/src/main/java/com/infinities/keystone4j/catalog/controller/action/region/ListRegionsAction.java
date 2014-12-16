package com.infinities.keystone4j.catalog.controller.action.region;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListRegionsAction extends AbstractRegionAction implements FilterProtectedAction<Region> {

	public ListRegionsAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(catalogApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Region> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Region> regions = this.getCatalogApi().listRegions(hints);
		CollectionWrapper<Region> wrapper = wrapCollection(request, regions, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_regions";
	}
}
