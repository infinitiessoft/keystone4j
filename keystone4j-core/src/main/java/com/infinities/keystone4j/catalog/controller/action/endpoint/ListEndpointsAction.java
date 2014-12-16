package com.infinities.keystone4j.catalog.controller.action.endpoint;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListEndpointsAction extends AbstractEndpointAction implements FilterProtectedAction<Endpoint> {

	public ListEndpointsAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(catalogApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Endpoint> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Endpoint> endpoints = this.getCatalogApi().listEndpoints(hints);
		CollectionWrapper<Endpoint> wrapper = wrapCollection(request, endpoints, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_endpoints";
	}
}
