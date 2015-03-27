package com.infinities.keystone4j.catalog.controller.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetServiceAction extends AbstractServiceAction implements ProtectedAction<Service> {

	private final String serviceid;


	public GetServiceAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, String serviceid) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.serviceid = serviceid;
	}

	@Override
	public MemberWrapper<Service> execute(ContainerRequestContext context) throws Exception {
		Service ref = this.getCatalogApi().getService(serviceid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_service";
	}
}