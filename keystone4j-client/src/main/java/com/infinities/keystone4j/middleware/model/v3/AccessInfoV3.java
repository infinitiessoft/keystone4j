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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.infinities.keystone4j.middleware.model.AccessInfo;
import com.infinities.keystone4j.middleware.model.ServiceCatalog;
import com.infinities.keystone4j.middleware.model.TokenWrapper;
import com.infinities.keystone4j.middleware.model.v3.wrapper.TokenV3Wrapper;

public class AccessInfoV3 extends AccessInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final com.infinities.keystone4j.middleware.model.v3.Token token;
	private final String authToken;
	private final static String version = "v3";


	public AccessInfoV3(String tokenid, com.infinities.keystone4j.middleware.model.v3.Token token) {
		// super();
		this.authToken = tokenid;
		this.token = token;
		this.serviceCatalog = ServiceCatalog.build(this, tokenid, this.getRegionName());
	}

	public static boolean isValid(TokenWrapper body) {
		if (body != null) {
			return (body instanceof TokenV3Wrapper);
		}

		return false;
	}

	@Override
	public String getRegionName() {
		return token.getRegionName();
	}

	@Override
	public boolean hasServiceCatalog() {
		return token.getCatalog() != null && !token.getCatalog().isEmpty();
	}

	@Override
	public String getAuthToken() {
		return authToken;
	}

	@Override
	public Calendar getExpires() {
		return token.getExpiresAt();
	}

	@Override
	public Calendar getIssued() {
		return token.getIssuedAt();
	}

	@Override
	public String getUsername() {
		return token.getUser().getName();
	}

	@Override
	public String getUserId() {
		return token.getUser().getId();
	}

	@Override
	public String getUserDomainId() {
		return token.getUser().getDomain().getId();
	}

	@Override
	public String getUserDomainName() {
		return token.getUser().getDomain().getName();
	}

	@Override
	public List<String> getRoleIds() {
		List<String> ids = new ArrayList<String>();
		List<com.infinities.keystone4j.middleware.model.v3.Token.Role> roles = token.getRoles();
		for (com.infinities.keystone4j.middleware.model.v3.Token.Role role : roles) {
			ids.add(role.getId());
		}

		return ids;
	}

	@Override
	public List<String> getRoleNames() {
		List<String> names = new ArrayList<String>();
		List<com.infinities.keystone4j.middleware.model.v3.Token.Role> roles = token.getRoles();
		for (com.infinities.keystone4j.middleware.model.v3.Token.Role role : roles) {
			names.add(role.getName());
		}

		return names;
	}

	@Override
	public String getDomainName() {
		try {
			return token.getDomain().getName();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getDomainId() {
		try {
			return token.getDomain().getId();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getProjectName() {
		try {
			return token.getProject().getName();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isScoped() {
		return (token.getCatalog() != null && !token.getCatalog().isEmpty() && token.getProject() != null);
	}

	@Override
	public boolean isProjectScoped() {
		return token.getProject() != null;
	}

	@Override
	public boolean isDomainScoped() {
		return token.getDomain() != null;
	}

	@Override
	public String getTrustId() {
		if (token.getTrust() == null) {
			return null;
		}

		return token.getTrust().getId();
	}

	@Override
	public boolean isTrustScoped() {
		return token.getTrust() != null;
	}

	@Override
	public String getTrusteeUserId() {
		if (token.getTrust() != null && token.getTrust().getTrustee() != null) {
			return token.getTrust().getTrustee().getId();
		}

		return null;
	}

	@Override
	public String getTrustorUserId() {
		if (token.getTrust() != null && token.getTrust().getTrustor() != null) {
			return token.getTrust().getTrustor().getId();
		}
		return null;
	}

	@Override
	public String getProjectId() {
		return token.getProject().getId();
	}

	@Override
	public String getProjectDomainId() {
		return token.getProject().getDomain().getId();
	}

	@Override
	public String getProjectDomainName() {
		return token.getProject().getDomain().getName();
	}

	@Override
	public List<URL> getAuthUrl() throws MalformedURLException {
		if (serviceCatalog != null) {
			return serviceCatalog.getURLs("identity", "public", this.getRegionName(), null);
		}
		return null;
	}

	public List<URL> getManagementUrl() throws MalformedURLException {
		if (serviceCatalog != null) {
			return serviceCatalog.getURLs("identity", "admin", this.getRegionName(), null);
		}
		return null;
	}

	@Override
	public String getVersion() {
		return AccessInfoV3.version;
	}

	@Override
	public String getOauthAccessTokenId() {
		if (token.getOauth1() == null) {
			return null;
		}
		return token.getOauth1().getAccessTokenId();
	}

	@Override
	public String getOauthConsumerId() {
		if (token.getOauth1() == null) {
			return null;
		}
		return token.getOauth1().getConsumerId();
	}

	public Token getToken() {
		return token;
	}

}
