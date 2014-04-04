package com.infinities.keystone4j.catalog.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.action.service.CreateServiceAction;
import com.infinities.keystone4j.catalog.action.service.DeleteServiceAction;
import com.infinities.keystone4j.catalog.action.service.GetServiceAction;
import com.infinities.keystone4j.catalog.action.service.ListServicesAction;
import com.infinities.keystone4j.catalog.action.service.UpdateServiceAction;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.ServiceWrapper;
import com.infinities.keystone4j.model.catalog.ServicesWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class ServiceV3ControllerImpl extends BaseController implements ServiceV3Controller {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public ServiceV3ControllerImpl(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public ServiceWrapper createService(Service service) {
		parMap.put("service", service);
		Action<Service> command = new PolicyCheckDecorator<Service>(new CreateServiceAction(catalogApi, service), null,
				tokenApi, policyApi, parMap);
		Service ret = command.execute(getRequest());
		return new ServiceWrapper(ret, getRequest());
	}

	@Override
	public ServicesWrapper listServices(String type, int page, int perPage) {
		parMap.put("type", type);
		Action<List<Service>> command = new FilterCheckDecorator<List<Service>>(new PaginateDecorator<Service>(
				new ListServicesAction(catalogApi, type), page, perPage), tokenApi, policyApi, parMap);
		List<Service> ret = command.execute(getRequest());
		return new ServicesWrapper(ret, getRequest());
	}

	@Override
	public ServiceWrapper getService(String serviceid) {
		parMap.put("serviceid", serviceid);
		Action<Service> command = new PolicyCheckDecorator<Service>(new GetServiceAction(catalogApi, serviceid), null,
				tokenApi, policyApi, parMap);
		Service ret = command.execute(getRequest());
		return new ServiceWrapper(ret, getRequest());
	}

	@Override
	public ServiceWrapper updateService(String serviceid, Service service) {
		parMap.put("serviceid", serviceid);
		parMap.put("service", service);
		Action<Service> command = new PolicyCheckDecorator<Service>(new UpdateServiceAction(catalogApi, serviceid, service),
				null, tokenApi, policyApi, parMap);
		Service ret = command.execute(getRequest());
		return new ServiceWrapper(ret, getRequest());
	}

	@Override
	public void deleteService(String serviceid) {
		parMap.put("serviceid", serviceid);
		Action<Service> command = new PolicyCheckDecorator<Service>(new DeleteServiceAction(catalogApi, serviceid), null,
				tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
