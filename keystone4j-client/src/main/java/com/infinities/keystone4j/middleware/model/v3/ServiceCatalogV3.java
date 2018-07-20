/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware.model.v3;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.infinities.keystone4j.middleware.model.AccessInfo;
import com.infinities.keystone4j.middleware.model.ServiceCatalog;
import com.infinities.keystone4j.middleware.model.TokenMetadata;

public class ServiceCatalogV3 extends ServiceCatalog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String authToken;
	private final AccessInfoV3 catalog;


	public ServiceCatalogV3(String token, AccessInfo accessInfo, String regionName) {
		super(regionName);
		this.authToken = token;
		this.catalog = (AccessInfoV3) accessInfo;
	}

	public static boolean isValid(AccessInfo accessInfo) {
		return (accessInfo instanceof AccessInfoV3);
	}

	@Override
	public TokenMetadata getToken() {
		TokenMetadata token = new TokenMetadata();
		token.setId(this.authToken);
		token.setExpires(this.catalog.getToken().getExpiresAt());
		token.setUserid(this.catalog.getToken().getUser().getId());
		Token.Domain domain = this.catalog.getToken().getDomain();
		if (domain != null) {
			token.setDomainId(domain.getId());
		}
		Token.Project project = this.catalog.getToken().getProject();
		if (project != null) {
			token.setTenantid(project.getId());
		}

		return token;
	}

	@Override
	public String normalizeEndpointType(String endpointType) {
		if (!Strings.isNullOrEmpty(endpointType)) {
			endpointType = endpointType.replaceAll("URL$", "");
		}

		return endpointType;
	}

	public boolean isEndpointTypeMatch(Token.Service.Endpoint endpoint, String endpointType) {
		if (endpointType.equals(endpoint.getInterface())) {
			return true;
		}
		return false;
	}

	public List<Token.Service> getData() {
		return catalog.getToken().getCatalog();
	}

	@Override
	public List<URL> getURLs(String serviceType, String endpointType, String regionName, String serviceName)
			throws MalformedURLException {
		List<Token.Service.Endpoint> endpoints = getServiceEndpoints(serviceType, endpointType, regionName, serviceName);

		List<URL> urls = new ArrayList<URL>();
		if (endpoints != null) {
			for (Token.Service.Endpoint endpoint : endpoints) {
				urls.add(new URL(endpoint.getUrl()));
			}
			return urls;
		}
		return null;

	}

	private List<Token.Service.Endpoint> getServiceEndpoints(String serviceType, String endpointType, String regionName,
			String serviceName) {
		Map<String, List<Token.Service.Endpoint>> scEndpoints = this.getEndpoints(serviceType, endpointType, regionName,
				serviceName);

		List<Token.Service.Endpoint> endpoints = scEndpoints.get(serviceType);
		if (endpoints == null) {
			return null;
		}

		return endpoints;
	}

	private Map<String, List<Token.Service.Endpoint>> getEndpoints(String serviceType, String endpointType,
			String regionName, String serviceName) {
		endpointType = normalizeEndpointType(endpointType);

		if (Strings.isNullOrEmpty(regionName)) {
			regionName = this.getRegionName();
		}

		Map<String, List<Token.Service.Endpoint>> sc = new HashMap<String, List<Token.Service.Endpoint>>();

		for (Token.Service service : this.getData()) {
			String st;
			st = service.getType();
			try {
				if (!Strings.isNullOrEmpty(serviceType) && !serviceType.equals(st)) {
					continue;
				}
			} catch (Exception e) {
				continue;
			}

			String sn;
			if (!Strings.isNullOrEmpty(serviceName)) {
				sn = service.getName();
				if (!serviceName.equals(sn)) {
					continue;
				}
			}

			sc.put(st, new ArrayList<Token.Service.Endpoint>());
			for (Token.Service.Endpoint endpoint : service.getEndpoints()) {
				if (!Strings.isNullOrEmpty(endpointType) && !this.isEndpointTypeMatch(endpoint, endpointType)) {
					continue;
				}

				if (!Strings.isNullOrEmpty(regionName) && !regionName.equals(endpoint.getRegion())) {
					continue;
				}

				sc.get(st).add(endpoint);
			}

		}

		return sc;
	}

}
