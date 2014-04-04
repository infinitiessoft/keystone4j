package com.infinities.keystone4j.catalog;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Service;

public interface CatalogApi extends Api {

	Endpoint createEndpoint(Endpoint endpoint);

	List<Endpoint> listEndpoints();

	Endpoint getEndpoint(String endpointid);

	Endpoint updateEndpoint(String endpointid, Endpoint endpoint);

	Endpoint deleteEndpoint(String endpointid);

	Service createService(Service service);

	List<Service> listServices();

	Service getService(String serviceid);

	Service updateService(String serviceid, Service service);

	Service deleteService(String serviceid);

	Catalog getV3Catalog(String userid, String projectid);

	// TODO getCatalog();

	// TODO region?

}
