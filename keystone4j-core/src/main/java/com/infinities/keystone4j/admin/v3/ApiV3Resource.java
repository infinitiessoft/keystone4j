package com.infinities.keystone4j.admin.v3;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.common.api.VersionApi;
import com.infinities.keystone4j.common.model.VersionWrapper;

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
		return OSInheritResource.class;
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

	@Path("/credentials")
	// credential
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

	@Path("/OS-EP-FILTER/trusts")
	// endpoint-filter
	public Class<EndpointFilterResource> getEndpointFilterResource() {
		return EndpointFilterResource.class;
	}

	@Path("/OS-SIMPLE-CERT")
	// trusts
	public Class<SimpleCertResource> getSimpleCertResource() {
		return SimpleCertResource.class;
	}

	@GET
	public VersionWrapper getVersions() throws MalformedURLException {
		return versionApi.getVersionV3();
	}

}
