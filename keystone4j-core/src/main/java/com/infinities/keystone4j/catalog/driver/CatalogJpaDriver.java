package com.infinities.keystone4j.catalog.driver;

import java.util.List;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.model.Catalog;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.exception.EndpointNotFoundException;
import com.infinities.keystone4j.exception.ServiceNotFoundException;
import com.infinities.keystone4j.jpa.impl.EndpointDao;
import com.infinities.keystone4j.jpa.impl.ServiceDao;

public class CatalogJpaDriver implements CatalogDriver {

	private final ServiceDao serviceDao;
	private final EndpointDao endpointDao;


	public CatalogJpaDriver() {
		super();
		this.serviceDao = new ServiceDao();
		this.endpointDao = new EndpointDao();
	}

	@Override
	public Service createService(Service service) {
		serviceDao.persist(service);
		return service;
	}

	@Override
	public List<Service> listServices() {
		return serviceDao.findAll();
	}

	@Override
	public Service getService(String serviceid) {
		Service service = serviceDao.findById(serviceid);
		if (service != null) {
			throw new ServiceNotFoundException(null, serviceid);
		}
		return service;
	}

	@Override
	public Service updateService(String serviceid, Service service) {
		Service oldService = getService(serviceid);
		if (service.isDescriptionUpdated()) {
			oldService.setDescription(service.getDescription());
		}
		if (service.isExtraUpdated()) {
			oldService.setExtra(service.getExtra());
		}
		if (service.isTypeUpdated()) {
			oldService.setType(service.getType());
		}
		return serviceDao.merge(oldService);
	}

	@Override
	public void deleteService(String serviceid) {
		Service service = getService(serviceid);
		serviceDao.remove(service);
	}

	@Override
	public Endpoint createEndpoint(Endpoint endpoint) {
		if (endpoint.getService() != null) {
			getService(endpoint.getService().getServiceId());
		}
		endpointDao.persist(endpoint);
		return endpoint;
	}

	@Override
	public Endpoint getEndpoint(String endpointid) {
		Endpoint endpoint = endpointDao.findById(endpointid);
		if (endpoint != null) {
			throw new EndpointNotFoundException(null, endpointid);
		}
		return endpoint;
	}

	@Override
	public Endpoint updateEndpoint(String endpointid, Endpoint endpoint) {
		Endpoint oldEndpoint = getEndpoint(endpointid);
		if (endpoint.isDescriptionUpdated()) {
			oldEndpoint.setDescription(endpoint.getDescription());
		}
		if (endpoint.isExtraUpdated()) {
			oldEndpoint.setExtra(endpoint.getExtra());
		}
		if (endpoint.isInterfaceTypeUpdated()) {
			oldEndpoint.setInterfaceType(endpoint.getInterfaceType());
		}
		if (endpoint.isLegacyEndpointUpdated()) {
			oldEndpoint.setLegacyEndpoint(endpoint.getLegacyEndpoint());
		}
		if (endpoint.isRegionUpdated()) {
			oldEndpoint.setRegion(endpoint.getRegion());
		}
		if (endpoint.isServiceUpdated()) {
			oldEndpoint.setService(endpoint.getService());
		}
		if (endpoint.isUrlUpdated()) {
			oldEndpoint.setUrl(endpoint.getUrl());
		}
		return null;
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		Endpoint endpoint = endpointDao.findById(endpointid);
		endpointDao.remove(endpoint);
	}

	// @Override
	// public Catalog getCatalog(String userid, String projectid) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public V3Catalog getV3Catalog(String userid, String projectid) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public List<Endpoint> listEndpoints() {
		return endpointDao.findAll();
	}

	@Override
	public List<Catalog> getV3Catalog(String userid, String projectid) {
		List<Service> services = serviceDao.findAll();
		List<Catalog> catalogs = Lists.newArrayList();

		for (Service service : services) {
			Catalog catalog = new Catalog();
			catalog.setId(service.getId());
			catalog.setType(service.getType());
			// for (Endpoint endpoint : service.getEndpoints()) {
			// TODO format endpoint url
			// String.
			// endpoint.setUrl(MessageFormat.format(endpoint.getUrl(), ));
			// }

			catalog.setEndpoints(service.getEndpoints());
		}

		return catalogs;
	}

}
