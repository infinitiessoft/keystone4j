/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.catalog.api;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.api.command.decorator.ResponseTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.GetCatalogCommand;
import com.infinities.keystone4j.catalog.api.command.GetV3CatalogCommand;
import com.infinities.keystone4j.catalog.api.command.endpoint.CreateEndpointCommand;
import com.infinities.keystone4j.catalog.api.command.endpoint.DeleteEndpointCommand;
import com.infinities.keystone4j.catalog.api.command.endpoint.GetEndpointCommand;
import com.infinities.keystone4j.catalog.api.command.endpoint.ListEndpointsCommand;
import com.infinities.keystone4j.catalog.api.command.endpoint.UpdateEndpointCommand;
import com.infinities.keystone4j.catalog.api.command.region.CreateRegionCommand;
import com.infinities.keystone4j.catalog.api.command.region.DeleteRegionCommand;
import com.infinities.keystone4j.catalog.api.command.region.GetRegionCommand;
import com.infinities.keystone4j.catalog.api.command.region.ListRegionsCommand;
import com.infinities.keystone4j.catalog.api.command.region.UpdateRegionCommand;
import com.infinities.keystone4j.catalog.api.command.service.CreateServiceCommand;
import com.infinities.keystone4j.catalog.api.command.service.DeleteServiceCommand;
import com.infinities.keystone4j.catalog.api.command.service.GetServiceCommand;
import com.infinities.keystone4j.catalog.api.command.service.ListServicesCommand;
import com.infinities.keystone4j.catalog.api.command.service.UpdateServiceCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.notification.Notifications;

public class CatalogApiImpl implements CatalogApi {

	private final CatalogDriver catalogDriver;
	private final static String _REGION = "region";
	private final static String _SERVICE = "service";
	private final static String _ENDPOINT = "endpoint";


	public CatalogApiImpl(CatalogDriver catalogDriver) {
		super();
		this.catalogDriver = catalogDriver;
	}

	@Override
	public Endpoint createEndpoint(String endpointid, Endpoint endpoint) throws Exception {
		NonTruncatedCommand<Endpoint> command = Notifications.created(new CreateEndpointCommand(catalogDriver, endpointid,
				endpoint), _ENDPOINT, false, 1, "id");
		return command.execute();
	}

	@Override
	public List<Endpoint> listEndpoints(Hints hints) throws Exception {
		TruncatedCommand<Endpoint> command = new ResponseTruncatedCommand<Endpoint>(new ListEndpointsCommand(catalogDriver),
				catalogDriver);
		return command.execute(hints);
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Endpoint getEndpoint(String endpointid) {
		GetEndpointCommand command = new GetEndpointCommand(catalogDriver, endpointid);
		return command.execute();
	}

	@Override
	public Endpoint updateEndpoint(String endpointid, Endpoint endpoint) throws Exception {
		NonTruncatedCommand<Endpoint> command = Notifications.updated(new UpdateEndpointCommand(catalogDriver, endpointid,
				endpoint), _ENDPOINT, false, 1, null);
		return command.execute();
	}

	@Override
	public Endpoint deleteEndpoint(String endpointid) throws Exception {
		NonTruncatedCommand<Endpoint> command = Notifications.deleted(new DeleteEndpointCommand(catalogDriver, endpointid),
				_ENDPOINT, false, 1, null);
		return command.execute();
	}

	@Override
	public Service createService(String serviceid, Service service) throws Exception {
		NonTruncatedCommand<Service> command = Notifications.created(new CreateServiceCommand(catalogDriver, service),
				_SERVICE, false, 1, "id");
		return command.execute();
	}

	@Override
	public List<Service> listServices(Hints hints) throws Exception {
		TruncatedCommand<Service> command = new ResponseTruncatedCommand<Service>(new ListServicesCommand(catalogDriver),
				catalogDriver);
		return command.execute(hints);
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Service getService(String serviceid) {
		GetServiceCommand command = new GetServiceCommand(catalogDriver, serviceid);
		return command.execute();
	}

	@Override
	public Service updateService(String serviceid, Service service) throws Exception {
		NonTruncatedCommand<Service> command = Notifications.updated(new UpdateServiceCommand(catalogDriver, serviceid,
				service), _SERVICE, false, 1, null);
		return command.execute();
	}

	@Override
	public Service deleteService(String serviceid) throws Exception {
		NonTruncatedCommand<Service> command = Notifications.deleted(new DeleteServiceCommand(catalogDriver, serviceid),
				_SERVICE, false, 1, null);
		return command.execute();
	}

	@Override
	public Map<String, Map<String, Map<String, String>>> getCatalog(String userid, String tenantid, Metadata metadata) {
		GetCatalogCommand command = new GetCatalogCommand(catalogDriver, userid, tenantid, metadata);
		return command.execute();
	}

	@Override
	public List<Service> getV3Catalog(String userid, String projectid) {
		GetV3CatalogCommand command = new GetV3CatalogCommand(catalogDriver, userid, projectid);
		return command.execute();
	}

	// TODO ignore @cache.on_arguments(should_cache_fn=SHOULD_CACHE,
	// expiration_time=EXPIRATION_TIME)
	@Override
	public Region getRegion(String regionid) {
		GetRegionCommand command = new GetRegionCommand(catalogDriver, regionid);
		return command.execute();
	}

	@Override
	public Region createRegion(Region region) throws Exception {
		NonTruncatedCommand<Region> command = Notifications.created(new CreateRegionCommand(catalogDriver, region), _REGION,
				false, 1, "id");
		return command.execute();
	}

	@Override
	public List<Region> listRegions(Hints hints) throws Exception {
		TruncatedCommand<Region> command = new ResponseTruncatedCommand<Region>(new ListRegionsCommand(catalogDriver),
				catalogDriver);
		return command.execute(hints);
	}

	@Override
	public Region updateRegion(String regionid, Region region) throws Exception {
		NonTruncatedCommand<Region> command = Notifications.updated(
				new UpdateRegionCommand(catalogDriver, regionid, region), _REGION, false, 1, null);
		return command.execute();
	}

	@Override
	public void deleteRegion(String regionid) throws Exception {
		NonTruncatedCommand<Region> command = Notifications.deleted(new DeleteRegionCommand(catalogDriver, regionid),
				_REGION, false, 1, null);
		command.execute();
	}

}
