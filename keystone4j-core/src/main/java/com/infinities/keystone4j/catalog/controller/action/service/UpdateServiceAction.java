package com.infinities.keystone4j.catalog.controller.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateServiceAction extends AbstractServiceAction implements ProtectedAction<Service> {

	private final String serviceid;
	private final Service service;


	public UpdateServiceAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, String serviceid, Service service) {
		super(catalogApi, tokenProviderApi);
		this.serviceid = serviceid;
		this.service = service;
	}

	@Override
	public MemberWrapper<Service> execute(ContainerRequestContext context) {
		requireMatchingId(serviceid, service);
		Service ref = this.getCatalogApi().updateService(serviceid, service);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_service";
	}
}
