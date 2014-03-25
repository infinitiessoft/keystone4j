package com.infinities.keystone4j.catalog;

import java.util.List;

import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.Service;

public interface CatalogDriver {

	Service createService(Service service);

	List<Service> listServices();

	Service getService(String serviceid);

	Service updateService(String serviceid, Service service);

	void deleteService(String serviceid);

	Endpoint createEndpoint(Endpoint endpoint);

	Endpoint getEndpoint(String endpointid);

	Endpoint updateEndpoint(String endpointid, Endpoint endpoint);

	void deleteEndpoint(String endpointid);

	// Catalog getCatalog(String userid, String projectid);
	//
	// Catalog getV3Catalog(String userid, String projectid);

	List<Endpoint> listEndpoints();

}
