package com.infinities.keystone4j.catalog;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.Service;

public interface CatalogApi extends Api {

	Endpoint createEndpoint(String id, Endpoint endpoint);

	List<Endpoint> listEndpoints(Hints hints);

	Endpoint getEndpoint(String endpointid);

	Endpoint updateEndpoint(String endpointid, Endpoint endpoint);

	Endpoint deleteEndpoint(String endpointid);

	Service createService(String id, Service service);

	List<Service> listServices(Hints hints);

	Service getService(String serviceid);

	Service updateService(String serviceid, Service service);

	Service deleteService(String serviceid);

	Catalog getV3Catalog(String userid, String projectid);

	Region getRegion(String regionid);

	Region createRegion(Region region);

	List<Region> listRegions(Hints hints);

	Region updateRegion(String regionid, Region region);

	void deleteRegion(String regionid);

	// TODO getCatalog();

	// TODO region?

}
