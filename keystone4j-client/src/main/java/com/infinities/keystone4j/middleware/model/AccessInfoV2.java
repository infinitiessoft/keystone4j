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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.infinities.keystone4j.middleware.model.Access.User.Role;
import com.infinities.keystone4j.middleware.model.wrapper.AccessWrapper;

public class AccessInfoV2 extends AccessInfo {

	private final static String version = "v2.0";
	private Access access;


	public AccessInfoV2(Access access) {
		this.access = access;
		this.serviceCatalog = ServiceCatalog.build(this, access.getToken().getId(), this.getRegionName());
	}

	public static boolean isValid(TokenWrapper body) {
		if (body != null) {
			return (body instanceof AccessWrapper);
		}

		return false;
	}

	@Override
	public String getRegionName() {
		return access.getRegionName();
	}

	@Override
	public boolean hasServiceCatalog() {
		return access.getServiceCatalog() != null && !access.getServiceCatalog().isEmpty();
	}

	@Override
	public String getAuthToken() {
		return access.getToken().getId();
	}

	@Override
	public Calendar getExpires() {
		return access.getToken().getExpires();
	}

	@Override
	public Calendar getIssued() {
		return access.getToken().getIssued_at();
	}

	@Override
	public String getUsername() {
		if (!Strings.isNullOrEmpty(access.getUser().getName())) {
			return access.getUser().getName();
		} else {
			return access.getUser().getUsername();
		}
	}

	@Override
	public String getUserId() {
		return access.getUser().getId();
	}

	@Override
	public String getUserDomainId() {
		return "default";
	}

	@Override
	public String getUserDomainName() {
		return "Default";
	}

	@Override
	public List<String> getRoleIds() {
		Set<String> ids = new HashSet<String>();
		if (access.getMetadata() != null && access.getMetadata().getRoles() != null) {
			ids.addAll(access.getMetadata().getRoles());
		}
		// if (access.getUser().getRoles() != null) {
		// for (Role role : access.getUser().getRoles()) {
		// ids.add(role.getId());
		// }
		// }
		return new ArrayList<String>(ids);
	}

	@Override
	public List<String> getRoleNames() {
		List<String> names = new ArrayList<String>();
		if (access.getUser().getRoles() != null) {
			for (Role role : access.getUser().getRoles()) {
				names.add(role.getName());
			}
		}
		return names;
	}

	@Override
	public String getDomainName() {
		return null;
	}

	@Override
	public String getDomainId() {
		return null;
	}

	@Override
	public String getProjectName() {
		try {
			return access.getToken().getTenant().getName();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isScoped() {
		if (access.getServiceCatalog() != null && !access.getServiceCatalog().isEmpty()
				&& access.getToken().getTenant() != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isProjectScoped() {
		return access.getToken().getTenant() != null;
	}

	@Override
	public boolean isDomainScoped() {
		return false;
	}

	@Override
	public String getTrustId() {
		if (access.getTrust() == null) {
			return null;
		}
		return access.getTrust().getId();
	}

	@Override
	public boolean isTrustScoped() {
		return access.getTrust() != null;
	}

	@Override
	public String getTrusteeUserId() {
		if (access.getTrust() == null) {
			return null;
		}
		return access.getTrust().getTrusteeUserId();
	}

	@Override
	public String getTrustorUserId() {
		return null;
	}

	@Override
	public String getProjectId() {
		try {
			return access.getToken().getTenant().getId();
		} catch (Exception e) {

		}
		
		try{
			return access.getUser().getTenantId();
		}catch(Exception e){
			
		}
		
		try{
			return access.getUser().getTenantId();
		}catch(Exception e){
			
		}
		
		return null;
	}

	@Override
	public String getProjectDomainId() {
		if (!Strings.isNullOrEmpty(getProjectId())) {
			return "default";
		}
		return null;
	}

	@Override
	public String getProjectDomainName() {
		if (!Strings.isNullOrEmpty(getProjectId())) {
			return "Default";
		}
		return null;
	}

	@Override
	public List<URL> getAuthUrl() throws MalformedURLException {
		if (serviceCatalog != null) {
			return serviceCatalog.getURLs("identity", "publicURL", this.getRegionName(), null);
		}
		return null;
	}

	public List<URL> getManagementUrl() throws MalformedURLException {
		if (serviceCatalog != null) {
			return serviceCatalog.getURLs("identity", "adminURL", this.getRegionName(), null);
		}
		return null;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getOauthAccessTokenId() {
		return null;
	}

	@Override
	public String getOauthConsumerId() {
		return null;
	}

	public Access getAccess() {
		return access;
	}
}
