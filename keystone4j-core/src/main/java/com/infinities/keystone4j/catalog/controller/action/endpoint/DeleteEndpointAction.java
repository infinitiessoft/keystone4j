package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteEndpointAction extends AbstractEndpointAction implements ProtectedAction<Endpoint> {

	private final String endpointid;


	public DeleteEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String endpointid) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.endpointid = endpointid;
	}

	@Override
	public MemberWrapper<Endpoint> execute(ContainerRequestContext context) {
		this.getCatalogApi().deleteEndpoint(endpointid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_endpoint";
	}
}
