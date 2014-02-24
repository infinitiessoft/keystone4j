package com.infinities.keystone4j.admin;

import java.net.MalformedURLException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.infinities.keystone4j.common.model.VersionApi;
import com.infinities.keystone4j.common.model.VersionWrapper;

public class AdminApiV2Resource {

	private VersionApi versionApi;


	public AdminApiV2Resource(Properties properties) {
		versionApi = new VersionApi(properties, "admin");
	}

	@Path("/users")
	public Class<UserResource> getUserResource() {
		return UserResource.class;
	}

	@Path("/tenants")
	public Class<TenantResource> getTenantResource() {
		return TenantResource.class;
	}

	@Path("/tokens")
	public Class<TokenResource> getTokenResource() {
		return TokenResource.class;
	}

	@Path("/certificates")
	public Class<CertificateResource> getCertificateResource() {
		return CertificateResource.class;
	}

	@GET
	@Produces("application/json")
	public VersionWrapper getVersions() throws MalformedURLException {
		return versionApi.getVersionV2();
	}

	@Path("/extensions")
	public Class<ExtensionResource> getExtensionResource() {
		return ExtensionResource.class;
	}

}
