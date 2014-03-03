package com.infinities.keystone4j.catalog.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.action.service.CreateServiceAction;
import com.infinities.keystone4j.catalog.action.service.DeleteServiceAction;
import com.infinities.keystone4j.catalog.action.service.GetServiceAction;
import com.infinities.keystone4j.catalog.action.service.ListServicesAction;
import com.infinities.keystone4j.catalog.action.service.UpdateServiceAction;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.catalog.model.ServiceWrapper;
import com.infinities.keystone4j.catalog.model.ServicesWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class ServiceV3ControllerImpl implements ServiceV3Controller {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	public ServiceV3ControllerImpl(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public ServiceWrapper createService(Service service) {
		Action<Service> command = new PolicyCheckDecorator<Service>(new CreateServiceAction(catalogApi, service), null,
				tokenApi, policyApi);
		Service ret = command.execute();
		return new ServiceWrapper(ret);
	}

	@Override
	public ServicesWrapper listServices(String type, int page, int perPage) {
		Action<List<Service>> command = new FilterCheckDecorator<List<Service>>(new PaginateDecorator<Service>(
				new ListServicesAction(catalogApi, type), page, perPage));

		List<Service> ret = command.execute();
		return new ServicesWrapper(ret);
	}

	@Override
	public ServiceWrapper getService(String serviceid) {
		Action<Service> command = new PolicyCheckDecorator<Service>(new GetServiceAction(catalogApi, serviceid), null,
				tokenApi, policyApi);
		Service ret = command.execute();
		return new ServiceWrapper(ret);
	}

	@Override
	public ServiceWrapper updateService(String serviceid, Service service) {
		Action<Service> command = new PolicyCheckDecorator<Service>(new UpdateServiceAction(catalogApi, serviceid, service),
				null, tokenApi, policyApi);
		Service ret = command.execute();
		return new ServiceWrapper(ret);
	}

	@Override
	public void deleteService(String serviceid) {
		Action<Service> command = new PolicyCheckDecorator<Service>(new DeleteServiceAction(catalogApi, serviceid), null,
				tokenApi, policyApi);
		command.execute();
	}

}
