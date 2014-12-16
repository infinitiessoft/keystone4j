package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateEndpointAction extends AbstractEndpointAction implements ProtectedAction<Endpoint> {

	private Endpoint endpoint;


	public CreateEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			Endpoint endpoint) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.endpoint = endpoint;
	}

	@Override
	public MemberWrapper<Endpoint> execute(ContainerRequestContext context) {
		assignUniqueId(endpoint);
		catalogApi.getService(endpoint.getServiceid());
		endpoint = validateEndpointRegion(endpoint);
		Endpoint ref = catalogApi.createEndpoint(endpoint.getId(), endpoint);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_endpoint";
	}
}
