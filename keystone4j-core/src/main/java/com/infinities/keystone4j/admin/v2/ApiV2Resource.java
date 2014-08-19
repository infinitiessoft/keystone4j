package com.infinities.keystone4j.admin.v2;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
	public VersionWrapper getVersions() throws MalformedURLException {
		return versionApi.getVersionV2();
	}

}
