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
package com.infinities.keystone4j.catalog;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.token.Metadata;

public interface CatalogDriver extends Driver {

	Service createService(Service service) throws Exception;

	List<Service> listServices(Hints hints) throws Exception;

	Service getService(String serviceid) throws Exception;

	Service updateService(String serviceid, Service service) throws Exception;

	Service deleteService(String serviceid) throws Exception;

	Endpoint createEndpoint(String endpointid, Endpoint endpoint) throws Exception;

	Endpoint getEndpoint(String endpointid) throws Exception;

	Endpoint updateEndpoint(String endpointid, Endpoint endpoint) throws Exception;

	void deleteEndpoint(String endpointid) throws Exception;

	// Catalog getCatalog(String userid, String projectid);
	//
	// Catalog getV3Catalog(String userid, String projectid);

	List<Endpoint> listEndpoints(Hints hints) throws Exception;

	Region createRegion(Region regionRef) throws Exception;

	Region getRegion(String regionid) throws Exception;

	Region updateRegion(String regionid, Region regionRef) throws Exception;

	Region deleteRegion(String regionid) throws Exception;

	List<Region> listRegions(Hints hints) throws Exception;

	// Catalog getCatalog(String userid, String projectid) throws Exception;

	List<Service> getV3Catalog(String userid, String projectid) throws Exception;

	Map<String, Map<String, Map<String, String>>> getCatalog(String userid, String tenantid, Metadata metadata);

}
