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
package com.infinities.keystone4j.admin.v2;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.model.common.VersionWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApiV2Resource {

	private final VersionApi versionApi;


	@Inject
	public ApiV2Resource(VersionApi versionApi) {
		this.versionApi = versionApi;
	}

	@Path("/tokens")
	// token
	public Class<TokenResource> getTokenResource() {
		return TokenResource.class;
	}

	@Path("/certificates")
	// token
	public Class<CertificateResource> getCertificateResource() {
		return CertificateResource.class;
	}

	@GET
	public VersionWrapper getVersions(@Context ContainerRequestContext context) throws MalformedURLException {
		return versionApi.getVersionV2(context);
	}

}
