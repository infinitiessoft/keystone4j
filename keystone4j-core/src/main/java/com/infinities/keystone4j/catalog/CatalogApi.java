package com.infinities.keystone4j.catalog;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.token.Metadata;

public interface CatalogApi extends Api {

	Endpoint createEndpoint(String id, Endpoint endpoint) throws Exception;

	List<Endpoint> listEndpoints(Hints hints) throws Exception;

	Endpoint getEndpoint(String endpointid) throws Exception;

	Endpoint updateEndpoint(String endpointid, Endpoint endpoint) throws Exception;

	Endpoint deleteEndpoint(String endpointid) throws Exception;

	Service createService(String id, Service service) throws Exception;

	List<Service> listServices(Hints hints) throws Exception;

	Service getService(String serviceid) throws Exception;

	Service updateService(String serviceid, Service service) throws Exception;

	Service deleteService(String serviceid) throws Exception;

	List<Service> getV3Catalog(String userid, String projectid) throws Exception;

	Region getRegion(String regionid) throws Exception;

	Region createRegion(Region region) throws Exception;

	List<Region> listRegions(Hints hints) throws Exception;

	Region updateRegion(String regionid, Region region) throws Exception;

	void deleteRegion(String regionid) throws Exception;

	Map<String, Map<String, Map<String, String>>> getCatalog(String userid, String tenantid, Metadata metadata)
			throws Exception;

	// TODO region?

}
