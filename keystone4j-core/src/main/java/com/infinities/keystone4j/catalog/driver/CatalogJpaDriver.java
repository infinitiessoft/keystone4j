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
package com.infinities.keystone4j.catalog.driver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.Table;
import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.driver.function.ListEndpointsFunction;
import com.infinities.keystone4j.catalog.driver.function.ListServicesFunction;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.TruncatedFunction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.EndpointDao;
import com.infinities.keystone4j.jpa.impl.RegionDao;
import com.infinities.keystone4j.jpa.impl.ServiceDao;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.option.Option;
import com.infinities.keystone4j.option.Options;

public class CatalogJpaDriver implements CatalogDriver {

	private final ServiceDao serviceDao;
	private final EndpointDao endpointDao;
	private final RegionDao regionDao;


	public CatalogJpaDriver() {
		super();
		this.serviceDao = new ServiceDao();
		this.endpointDao = new EndpointDao();
		this.regionDao = new RegionDao();
	}

	@Override
	public List<Region> listRegions(Hints hints) throws SecurityException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return regionDao.listRegion(hints);
	}

	@Override
	public Region getRegion(String regionId) {
		Region ref = regionDao.findById(regionId);
		if (ref == null) {
			throw Exceptions.RegionNotFoundException.getInstance(null, regionId);
		}
		return ref;
	}

	@Override
	public Region deleteRegion(String regionId) {
		Region ref = getRegion(regionId);
		if (hasEndpoints(ref)) {
			throw Exceptions.RegionDeletionError.getInstance(null, regionId);
		}
		deleteChildRegions(regionId);
		regionDao.remove(ref);
		return null;
	}

	private void deleteChildRegions(String regionId) {
		List<Region> children = regionDao.listByParentRegionId(regionId);
		for (Region child : children) {
			deleteChildRegions(child.getId());
			regionDao.remove(child);

		}
	}

	private boolean hasEndpoints(Region region) {
		if (region.getEndpoints() != null && !region.getEndpoints().isEmpty()) {
			return true;
		}
		List<Region> children = regionDao.listByParentRegionId(region.getId());

		for (Region child : children) {
			if (hasEndpoints(child)) {
				return true;
			}
		}

		return false;
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='region')
	@Override
	public Region createRegion(Region regionRef) {
		checkParentRegion(regionRef);
		regionDao.persist(regionRef);
		return regionRef;
	}

	@Override
	public Region updateRegion(String regionId, Region regionRef) {
		checkParentRegion(regionRef);
		Region oldRegion = getRegion(regionId);
		if (regionRef.isDescriptionUpdated()) {
			oldRegion.setDescription(regionRef.getDescription());
		}
		if (regionRef.isExtraUpdated()) {
			oldRegion.setExtra(regionRef.getExtra());
		}
		if (regionRef.isNameUpdated()) {
			oldRegion.setName(regionRef.getName());
		}
		if (regionRef.isUrlUpdated()) {
			oldRegion.setUrl(regionRef.getUrl());
		}
		return regionDao.merge(oldRegion);
	}

	private void checkParentRegion(Region regionRef) {
		String parentRegionId = regionRef.getParentRegionId();
		if (!Strings.isNullOrEmpty(parentRegionId)) {
			getRegion(parentRegionId);
		}
	}

	@Override
	public Service createService(Service service) {
		serviceDao.persist(service);
		return service;
	}

	@Override
	public List<Service> listServices(Hints hints) throws Exception {
		ListFunction<Service> function = new TruncatedFunction<Service>(new ListServicesFunction());
		return function.execute(hints);
	}

	@Override
	public Service getService(String serviceid) {
		Service service = serviceDao.findById(serviceid);
		if (service == null) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
		return service;
	}

	@Override
	public Service deleteService(String serviceid) {
		Service service = getService(serviceid);
		serviceDao.remove(service);
		return null;
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
		if (service.isNameUpdated()) {
			oldService.setName(service.getName());
		}
		return serviceDao.merge(oldService);
	}

	@Override
	public Endpoint createEndpoint(String endpointId, Endpoint endpoint) {
		getService(endpoint.getServiceid());
		if (!Strings.isNullOrEmpty(endpoint.getRegionid())) {
			getRegion(endpoint.getRegionid());
		}

		endpointDao.persist(endpoint);
		return endpoint;
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		Endpoint endpoint = endpointDao.findById(endpointid);
		endpointDao.remove(endpoint);
	}

	@Override
	public Endpoint getEndpoint(String endpointid) {
		Endpoint endpoint = endpointDao.findById(endpointid);
		if (endpoint == null) {
			throw Exceptions.EndpointNotFoundException.getInstance(null, endpointid);
		}
		return endpoint;
	}

	@Override
	public List<Endpoint> listEndpoints(Hints hints) throws Exception {
		ListFunction<Endpoint> function = new TruncatedFunction<Endpoint>(new ListEndpointsFunction());
		return function.execute(hints);
	}

	@Override
	public Endpoint updateEndpoint(String endpointid, Endpoint endpoint) {
		Endpoint oldEndpoint = getEndpoint(endpointid);
		if (endpoint.isNameUpdated()) {
			oldEndpoint.setName(endpoint.getName());
		}
		if (endpoint.isDescriptionUpdated()) {
			oldEndpoint.setDescription(endpoint.getDescription());
		}
		if (endpoint.isExtraUpdated()) {
			oldEndpoint.setExtra(endpoint.getExtra());
		}
		if (endpoint.isInterfaceTypeUpdated()) {
			oldEndpoint.setInterfaceType(endpoint.getInterfaceType());
		}
		if (endpoint.isLegacyEndpointIdUpdated()) {
			oldEndpoint.setLegacyEndpointId(endpoint.getLegacyEndpointId());
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
		return endpointDao.merge(oldEndpoint);
	}

	@Override
	public Integer getListLimit() {
		return Config.Instance.getOpt(Config.Type.catalog, "list_limit").asInteger();
	}

	@Override
	public Map<String, Map<String, Map<String, String>>> getCatalog(String userid, String tenantid, Metadata metadata) {
		Table<Type, String, Option> substitutions = Config.Instance.getTable();
		substitutions.put(Type.DEFAULT, "tenant_id", Options.newStrOpt("tenant_id", tenantid));
		substitutions.put(Type.DEFAULT, "user_id", Options.newStrOpt("user_id", userid));

		List<Endpoint> endpoints = endpointDao.listAllEnabled();

		Map<String, Map<String, Map<String, String>>> catalog = new HashMap<String, Map<String, Map<String, String>>>();

		for (Endpoint endpoint : endpoints) {
			if (!endpoint.getService().getEnabled()) {
				continue;
			}
			String url = null;
			try {
				url = formatUrl(endpoint.getUrl(), substitutions);
			} catch (Exception e) {
				continue;
			}

			String region = endpoint.getRegionid();
			String serviceType = endpoint.getService().getType();
			Map<String, String> defaultService = new HashMap<String, String>();
			defaultService.put("id", endpoint.getId());
			defaultService.put("name", endpoint.getService().getName());
			defaultService.put("publicURL", "");
			Map<String, Map<String, String>> regionDict = new HashMap<String, Map<String, String>>();
			catalog.put(region, regionDict);
			regionDict.put(serviceType, defaultService);
			String interfaceUrl = String.format("%sURL", endpoint.getInterfaceType());
			defaultService.put(interfaceUrl, url);
		}

		return catalog;
	}

	@Override
	public List<Service> getV3Catalog(String userid, String projectid) throws Exception {
		Table<Type, String, Option> substitutions = Config.Instance.getTable();
		substitutions.put(Type.DEFAULT, "tenant_id", Options.newStrOpt("tenant_id", projectid));
		substitutions.put(Type.DEFAULT, "user_id", Options.newStrOpt("user_id", userid));
		List<Service> services = serviceDao.listAllEnabled();
		List<Service> refs = new ArrayList<Service>();
		for (Service svc : services) {
			refs.add(makeV3Service(svc, substitutions));
		}
		return refs;
	}

	private Service makeV3Service(Service svc, Table<Type, String, Option> substitutions) {
		Service service = new Service();
		service.setId(svc.getId());
		service.setType(svc.getType());
		service.setName(svc.getName());
		service.setEndpoints(makeV3Endponts(svc.getEndpoints(), substitutions));
		return service;
	}

	private Set<Endpoint> makeV3Endponts(Set<Endpoint> endpoints, Table<Type, String, Option> substitutions) {
		Set<Endpoint> refs = new HashSet<Endpoint>();
		for (Endpoint endpoint : endpoints) {
			if (endpoint.getEnabled()) {
				// endpoint.setServiceid(null);
				// endpoint.setLegacyEndpointId(null);
				// endpoint.setEnabled(null);
				endpoint.setRegion(endpoint.getRegionid());
				try {
					endpoint.setUrl(formatUrl(endpoint.getUrl(), substitutions));
				} catch (Exception e) {

				}
				refs.add(endpoint);
			}
		}
		return refs;
	}

	private String formatUrl(String url, Table<Type, String, Option> substitutions) {
		List<String> WHIELISTED_PROPERTIES = new ArrayList<String>();
		WHIELISTED_PROPERTIES.add("tenant_id");
		WHIELISTED_PROPERTIES.add("user_id");
		WHIELISTED_PROPERTIES.add("public_bind_host");
		WHIELISTED_PROPERTIES.add("admin_bind_host");
		WHIELISTED_PROPERTIES.add("compute_host");
		WHIELISTED_PROPERTIES.add("compute_port");
		WHIELISTED_PROPERTIES.add("admin_port");
		WHIELISTED_PROPERTIES.add("public_port");
		WHIELISTED_PROPERTIES.add("public_endpoint");
		WHIELISTED_PROPERTIES.add("admin_endpoint");

		String result = url.replace("$(", "%(");
		result = replaceVarWithTable(result, substitutions);

		return result;
	}

	public static String replaceVarWithTable(String original, Table<Type, String, Option> substitutions) {
		Pattern pattern = Pattern.compile("%\\((.+?)\\)s");
		Matcher matcher = pattern.matcher(original);
		StringBuffer replace = new StringBuffer(original);
		boolean nextRound = true;
		while (nextRound) {
			nextRound = false;
			while (matcher.find()) {
				String match = matcher.group(1);
				int start = matcher.start(1);
				int end = matcher.end(1);
				String value = substitutions.get(Config.Type.DEFAULT, match).asText();
				replace.replace(start, end, value);
				nextRound = true;
			}
		}
		return replace.toString();
	}
	// @Override
	// public Catalog getV3Catalog(String userid, String projectid) {
	// List<Service> services = serviceDao.findAll();
	// List<Catalog> catalogs = Lists.newArrayList();
	//
	// for (Service service : services) {
	// Catalog catalog = new Catalog();
	// catalog.setId(service.getId());
	// catalog.setType(service.getType());
	// // for (Endpoint endpoint : service.getEndpoints()) {
	// // TODO format endpoint url
	// // String.
	// // endpoint.setUrl(MessageFormat.format(endpoint.getUrl(), ));
	// // }
	//
	// catalog.setEndpoints(service.getEndpoints());
	// }
	//
	// return catalog;
	// }

}
