package com.infinities.keystone4j.catalog.api;

import java.util.List;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.GetV3CatalogCommand;
import com.infinities.keystone4j.catalog.command.endpoint.CreateEndpointCommand;
import com.infinities.keystone4j.catalog.command.endpoint.DeleteEndpointCommand;
import com.infinities.keystone4j.catalog.command.endpoint.GetEndpointCommand;
import com.infinities.keystone4j.catalog.command.endpoint.ListEndpointsCommand;
import com.infinities.keystone4j.catalog.command.endpoint.UpdateEndpointCommand;
import com.infinities.keystone4j.catalog.command.service.CreateServiceCommand;
import com.infinities.keystone4j.catalog.command.service.DeleteServiceCommand;
import com.infinities.keystone4j.catalog.command.service.GetServiceCommand;
import com.infinities.keystone4j.catalog.command.service.ListServicesCommand;
import com.infinities.keystone4j.catalog.command.service.UpdateServiceCommand;
import com.infinities.keystone4j.catalog.model.Catalog;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.Service;

public class CatalogApiImpl implements CatalogApi {

	private final CatalogDriver catalogDriver;


	public CatalogApiImpl(CatalogDriver catalogDriver) {
		super();
		this.catalogDriver = catalogDriver;
	}

	@Override
	public Endpoint createEndpoint(Endpoint endpoint) {
		CreateEndpointCommand command = new CreateEndpointCommand(catalogDriver, endpoint);
		return command.execute();
	}

	@Override
	public List<Endpoint> listEndpoints() {
		ListEndpointsCommand command = new ListEndpointsCommand(catalogDriver);
		return command.execute();
	}

	@Override
	public Endpoint getEndpoint(String endpointid) {
		GetEndpointCommand command = new GetEndpointCommand(catalogDriver, endpointid);
		return command.execute();
	}

	@Override
	public Endpoint updateEndpoint(String endpointid, Endpoint endpoint) {
		UpdateEndpointCommand command = new UpdateEndpointCommand(catalogDriver, endpointid, endpoint);
		return command.execute();
	}

	@Override
	public Endpoint deleteEndpoint(String endpointid) {
		DeleteEndpointCommand command = new DeleteEndpointCommand(catalogDriver, endpointid);
		return command.execute();
	}

	@Override
	public Service createService(Service service) {
		CreateServiceCommand command = new CreateServiceCommand(catalogDriver, service);
		return command.execute();
	}

	@Override
	public List<Service> listServices() {
		ListServicesCommand command = new ListServicesCommand(catalogDriver);
		return command.execute();
	}

	@Override
	public Service getService(String serviceid) {
		GetServiceCommand command = new GetServiceCommand(catalogDriver, serviceid);
		return command.execute();
	}

	@Override
	public Service updateService(String serviceid, Service service) {
		UpdateServiceCommand command = new UpdateServiceCommand(catalogDriver, serviceid, service);
		return command.execute();
	}

	@Override
	public Service deleteService(String serviceid) {
		DeleteServiceCommand command = new DeleteServiceCommand(catalogDriver, serviceid);
		return command.execute();
	}

	@Override
	public Catalog getV3Catalog(String userid, String projectid) {
		GetV3CatalogCommand command = new GetV3CatalogCommand(catalogDriver, userid, projectid);
		return command.execute();
	}

}
