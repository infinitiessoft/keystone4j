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
package com.infinities.keystone4j.admin.v3;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.common.VersionWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApiV3Resource {

	private final VersionApi versionApi;


	@Inject
	public ApiV3Resource(VersionApi versionApi) {
		this.versionApi = versionApi;
	}

	@Path("/users")
	// assignment
	public Class<UserV3Resource> getUserV3Resource() {
		return UserV3Resource.class;
	}

	@Path("/roles")
	// assignment
	public Class<RoleV3Resource> getRoleV3Resource() {
		return RoleV3Resource.class;
	}

	@Path("/role_assignments")
	// assignment
	public Class<RoleAssignmentV3Resource> getRoleAssignmentV3Resource() {
		return RoleAssignmentV3Resource.class;
	}

	@Path("/projects")
	// assignment
	public Class<ProjectResource> getProjectResource() {
		return ProjectResource.class;
	}

	@Path("/domains")
	// assignment
	public Class<DomainResource> getDomainResource() {
		return DomainResource.class;
	}

	@Path("/OS-INHERIT")
	// assignment
	public Class<OSInheritResource> getOSInheritResource() {
		if (Config.getOpt(Config.Type.os_inherit, "enabled").asBoolean()) {
			return OSInheritResource.class;
		} else {
			throw Exceptions.NotFoundException.getInstance();
		}
	}

	@Path("/auth")
	// auth
	public Class<AuthResource> getAuthResource() {
		return AuthResource.class;
	}

	@Path("/services")
	// catalog
	public Class<ServiceResource> getServiceResource() {
		return ServiceResource.class;
	}

	@Path("/endpoints")
	// catalog
	public Class<EndpointResource> getEndpointResource() {
		return EndpointResource.class;
	}

	@Path("/regions")
	// catalog
	public Class<RegionResource> getRegionResource() {
		return RegionResource.class;
	}

	@Path("/credentials")
	// credential user for ec2 auth
	public Class<CredentialResource> getCredentialResource() {
		return CredentialResource.class;
	}

	@Path("/groups")
	// identity
	public Class<GroupResource> getGroupResource() {
		return GroupResource.class;
	}

	@Path("/policies")
	// policies
	public Class<PolicyResource> getPolicyResource() {
		return PolicyResource.class;
	}

	@Path("/OS-TRUST/trusts")
	// trusts
	public Class<TrustResource> getTrustResource() {
		return TrustResource.class;
	}

	// @Path("/OS-EP-FILTER/trusts")
	// // endpoint-filter
	// public Class<EndpointFilterResource> getEndpointFilterResource() {
	// return EndpointFilterResource.class;
	// }

	// keystone.contrib.simple_cert.routers 20141129
	@Path("/OS-SIMPLE-CERT")
	// trusts
	public Class<SimpleCertResource> getSimpleCertResource() {
		return SimpleCertResource.class;
	}

	@GET
	public VersionWrapper getVersions(@Context ContainerRequestContext context) throws MalformedURLException {
		return versionApi.getVersionV3(context);
	}

}
