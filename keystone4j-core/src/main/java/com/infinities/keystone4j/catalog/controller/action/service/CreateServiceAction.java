package com.infinities.keystone4j.catalog.controller.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateServiceAction extends AbstractServiceAction implements ProtectedAction<Service> {

	private final Service service;


	public CreateServiceAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, Service service) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.service = service;
	}

	@Override
	public MemberWrapper<Service> execute(ContainerRequestContext context) throws Exception {
		assignUniqueId(service);
		Service ref = catalogApi.createService(service.getId(), service);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_service";
	}
}
