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
package com.infinities.keystone4j.middleware.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.infinities.keystone4j.middleware.model.v3.ServiceCatalogV3;

public abstract class ServiceCatalog {

	private String regionName;


	protected ServiceCatalog(String regionName) {
		this.regionName = regionName;
	}

	public String getRegionName() {
		return this.regionName;
	}

	public abstract TokenMetadata getToken();

	// public abstract boolean isEndpointTypeMatch(Endpoint endpoint, String
	// endpointType);

	public abstract String normalizeEndpointType(String endpointType);
	
	public abstract List<URL> getURLs(String serviceType, String endpointType, String regionName, String serviceName) throws MalformedURLException;

	public static ServiceCatalog build(AccessInfo accessInfo, String token, String regionName) {
		if (ServiceCatalogV3.isValid(accessInfo)) {
			return new ServiceCatalogV3(token, accessInfo, regionName);
		} else if (ServiceCatalogV2.isValid(accessInfo)) {
			return new ServiceCatalogV2(accessInfo, regionName);
		} else {
			throw new RuntimeException("Unrecognized auth response");
		}
	}
}
