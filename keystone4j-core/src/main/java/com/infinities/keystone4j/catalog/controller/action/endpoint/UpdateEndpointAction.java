package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateEndpointAction extends AbstractEndpointAction implements ProtectedAction<Endpoint> {

	private final static Logger logger = LoggerFactory.getLogger(UpdateEndpointAction.class);
	private final String endpointid;
	private Endpoint endpoint;


	public UpdateEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String endpointid, Endpoint endpoint) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public MemberWrapper<Endpoint> execute(ContainerRequestContext context) throws Exception {
		logger.debug("get endpoint: {}", endpointid);
		requireMatchingId(endpointid, endpoint);
		if (!Strings.isNullOrEmpty(endpoint.getServiceid())) {
			catalogApi.getService(endpoint.getServiceid());
		}
		endpoint = validateEndpointRegion(endpoint);
		Endpoint ref = this.getCatalogApi().updateEndpoint(endpointid, endpoint);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_endpoint";
	}
}
