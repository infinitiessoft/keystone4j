package com.infinities.keystone4j.catalog.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.catalog.controller.action.service.CreateServiceAction;
import com.infinities.keystone4j.catalog.controller.action.service.DeleteServiceAction;
import com.infinities.keystone4j.catalog.controller.action.service.GetServiceAction;
import com.infinities.keystone4j.catalog.controller.action.service.ListServicesAction;
import com.infinities.keystone4j.catalog.controller.action.service.UpdateServiceAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.catalog.controllers.ServiceV3 20141211

public class ServiceV3ControllerImpl extends BaseController implements ServiceV3Controller {

	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public ServiceV3ControllerImpl(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	// TODO Ignore validation.validated(schema.service_create,'service')
	@Override
	public MemberWrapper<Service> createService(Service service) throws Exception {
		ProtectedAction<Service> command = new ProtectedDecorator<Service>(new CreateServiceAction(catalogApi,
				tokenProviderApi, service), tokenProviderApi, policyApi);
		MemberWrapper<Service> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Service> listServices() throws Exception {
		FilterProtectedAction<Service> command = new FilterProtectedDecorator<Service>(new ListServicesAction(catalogApi,
				tokenProviderApi), tokenProviderApi, policyApi);
		CollectionWrapper<Service> ret = command.execute(getRequest(), "type", "name");
		return ret;
	}

	@Override
	public MemberWrapper<Service> getService(String serviceid) {
		Service ref = getMemberFromDriver(serviceid);
		ProtectedAction<Service> command = new ProtectedDecorator<Service>(new GetServiceAction(catalogApi,
				tokenProviderApi, serviceid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Service> ret = command.execute(getRequest());
		return ret;
	}

	// TODO Ignore validation.validated(schema.service_update,'service')
	@Override
	public MemberWrapper<Service> updateService(String serviceid, Service service) {
		Service ref = getMemberFromDriver(serviceid);
		ProtectedAction<Service> command = new ProtectedDecorator<Service>(new UpdateServiceAction(catalogApi,
				tokenProviderApi, serviceid, service), tokenProviderApi, policyApi, ref);
		MemberWrapper<Service> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteService(String serviceid) {
		Service ref = getMemberFromDriver(serviceid);
		ProtectedAction<Service> command = new ProtectedDecorator<Service>(new DeleteServiceAction(catalogApi,
				tokenProviderApi, serviceid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	public Service getMemberFromDriver(String serviceid) {
		return catalogApi.getService(serviceid);
	}

}
