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
package com.infinities.keystone4j.token.controller.action;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.Access.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ListEndpointsAction extends AbstractTokenAction {

	private final static Logger logger = LoggerFactory.getLogger(ListEndpointsAction.class);
	private final String tokenid;


	public ListEndpointsAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi, String tokenid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, trustApi, policyApi);
		this.tokenid = tokenid;
	}

	public Service execute(ContainerRequestContext request) throws Exception {
		logger.debug("list endpoints for tokenid: {}", tokenid);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		assertAdmin(context);
		KeystoneToken tokenRef = getTokenRef(tokenid, null);
		Map<String, Map<String, Map<String, String>>> catalogRef = null;
		logger.debug("token project id: {}", tokenRef.getProjectId());
		if (!Strings.isNullOrEmpty(tokenRef.getProjectId())) {
			catalogRef = catalogApi.getCatalog(tokenRef.getUserId(), tokenRef.getProjectId(), tokenRef.getMetadata());
		}

		return formatEndpointList(catalogRef);
	}

	// belongsTo = null
	private KeystoneToken getTokenRef(String tokenid, String belongsTo) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException, Exception {
		KeystoneToken tokenRef = new KeystoneToken(tokenid, this.getTokenProviderApi().validateToken(tokenid, null));
		if (!Strings.isNullOrEmpty(belongsTo)) {
			if (!tokenRef.isProjectScoped()) {
				throw Exceptions.UnauthorizedException.getInstance("Token does not being to specified tenant.");
			}

			if (!belongsTo.equals(tokenRef.getProjectId())) {
				throw Exceptions.UnauthorizedException.getInstance("Token does not being to specified tenant.");
			}
		}
		return tokenRef;
	}

	private Service formatEndpointList(Map<String, Map<String, Map<String, String>>> catalogRef) {
		if (catalogRef == null) {
			return new Access.Service();
		}

		// EndpointsV2Wrapper endpoints = new EndpointsV2Wrapper();
		List<Access.Service.Endpoint> endpoints = new ArrayList<Access.Service.Endpoint>();
		for (Entry<String, Map<String, Map<String, String>>> entry : catalogRef.entrySet()) {
			String regionName = entry.getKey();
			Map<String, Map<String, String>> regionRef = entry.getValue();
			for (Entry<String, Map<String, String>> entry2 : regionRef.entrySet()) {
				String serviceType = entry2.getKey();
				Map<String, String> serviceRef = entry2.getValue();
				Access.Service.Endpoint endpoint = new Access.Service.Endpoint();
				endpoint.setId(serviceRef.get("id"));
				endpoint.setName(serviceRef.get("name"));
				endpoint.setType(serviceType);
				endpoint.setRegion(regionName);
				endpoint.setPublicURL(serviceRef.get("publicURL"));
				endpoint.setInternalURL(serviceRef.get("internalURL"));
				endpoint.setAdminURL(serviceRef.get("adminURL"));
				endpoints.add(endpoint);
			}
		}

		Access.Service service = new Access.Service();
		service.setEndpoints(endpoints);
		return service;
	}
}
