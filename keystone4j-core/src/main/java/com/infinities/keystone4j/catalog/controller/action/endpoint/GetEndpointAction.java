package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetEndpointAction extends AbstractEndpointAction implements ProtectedAction<Endpoint> {

	private final static Logger logger = LoggerFactory.getLogger(GetEndpointAction.class);
	private final String endpointid;


	public GetEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String endpointid) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.endpointid = endpointid;
	}

	@Override
	public MemberWrapper<Endpoint> execute(ContainerRequestContext context) throws Exception {
		Endpoint ref = this.getCatalogApi().getEndpoint(endpointid);
		logger.debug("get endpoint: {}", endpointid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_endpoint";
	}
}
