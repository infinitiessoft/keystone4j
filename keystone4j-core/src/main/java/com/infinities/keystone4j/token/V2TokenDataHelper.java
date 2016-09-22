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
package com.infinities.keystone4j.token;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.Access.Service;
import com.infinities.keystone4j.model.token.v2.Access.Service.Endpoint;
import com.infinities.keystone4j.model.token.v2.TokenV2;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.provider.driver.BaseProvider;

public class V2TokenDataHelper {

	public TokenV2DataWrapper formatToken(IToken tokenRef, List<Role> rolesRef,
			Map<String, Map<String, Map<String, String>>> catalogRef, Trust trustRef) throws UnsupportedEncodingException {
		List<String> auditInfo = null;
		User userRef = tokenRef.getUser();
		Metadata metadataRef = tokenRef.getMetadata();
		if (rolesRef == null) {
			rolesRef = new ArrayList<Role>();
		}
		Calendar expires = tokenRef.getExpires();
		if (expires == null) {
			expires = BaseProvider.getDefaultExpireTime();
		}

		ITokenDataWrapper tokenData = tokenRef.getTokenData();
		if (tokenData != null) {
			List<String> tokenAudit = ((TokenV2DataWrapper) tokenData).getAccess().getToken().getAuditIds();
			auditInfo = tokenAudit;
		}

		if (auditInfo == null) {
			auditInfo = BaseProvider.auditInfo(tokenRef.getParentAuditId());
		}

		Access access = new Access();
		access.setMetadata(new Metadata());
		TokenV2 token = new TokenV2();
		access.setToken(token);
		token.setId(tokenRef.getId());
		token.setExpires(expires);
		token.setIssued_at(Calendar.getInstance());
		token.setAuditIds(auditInfo);

		Access.User user = new Access.User();
		access.setUser(user);
		user.setId(userRef.getId());
		user.setName(userRef.getName());
		user.setUsername(userRef.getName());
		List<Access.User.Role> roles = new ArrayList<Access.User.Role>();
		for (Role role : rolesRef) {
			Access.User.Role r = new Access.User.Role();
			r.setId(role.getId());
			r.setName(role.getName());
			roles.add(r);
		}
		user.setRoles(roles);
		user.setRolesLinks(metadataRef.getRolesLinks());

		if (tokenRef.getBind() != null) {
			access.getToken().setBind(tokenRef.getBind());
		}
		if (tokenRef.getTenant() != null) {
			tokenRef.getTenant().setEnabled(true);
			access.getToken().setTenant(tokenRef.getTenant());
		}
		if (catalogRef != null) {
			access.setServiceCatalog(formatCatalog(catalogRef));
		}
		if (metadataRef != null) {
			if (metadataRef.getIsAdmin() != null) {
				access.getMetadata().setIsAdmin(metadataRef.getIsAdmin());
			} else {
				access.getMetadata().setIsAdmin(0);
			}
		}
		if (metadataRef.getRoles() != null) {
			access.getMetadata().setRoles(new ArrayList<String>(metadataRef.getRoles()));
		}
		if (Config.getOpt(Config.Type.trust, "enabled").asBoolean() && trustRef != null) {
			Access.Trust trust = new Access.Trust();
			trust.setTrusteeUserId(trustRef.getTrusteeUserId());
			trust.setId(trustRef.getId());
			trust.setTrustorUserId(trustRef.getTrustorUserId());
			trust.setImpersonation(trustRef.getImpersonation());
		}

		TokenV2DataWrapper wrapper = new TokenV2DataWrapper();
		wrapper.setAccess(access);
		return wrapper;
	}

	private List<Service> formatCatalog(Map<String, Map<String, Map<String, String>>> catalogRef) {
		if (catalogRef == null) {
			return new ArrayList<Service>();
		}

		Map<String, Service> services = new HashMap<String, Service>();
		for (Entry<String, Map<String, Map<String, String>>> entry : catalogRef.entrySet()) {
			String region = entry.getKey();
			Map<String, Map<String, String>> regionRef = entry.getValue();
			for (Entry<String, Map<String, String>> entry2 : regionRef.entrySet()) {
				String service = entry2.getKey();
				Map<String, String> serviceRef = entry2.getValue();
				Service newServiceRef = services.get(service);
				if (newServiceRef == null) {
					newServiceRef = new Service();
				}
				newServiceRef.setName(serviceRef.get("name"));
				newServiceRef.setType(service);
				serviceRef.put("region", region);

				List<Endpoint> endpointsRef = newServiceRef.getEndpoints();
				Endpoint endpoint = new Endpoint();
				endpoint.setRegion(serviceRef.get("region"));
				endpoint.setAdminURL(serviceRef.get("adminURL"));
				endpoint.setInternalURL(serviceRef.get("internalURL"));
				endpoint.setPublicURL(serviceRef.get("publicURL"));
				endpointsRef.add(endpoint);

				services.put(service, newServiceRef);
			}
		}

		return new ArrayList<Service>(services.values());
	}
}
