package com.infinities.keystone4j.admin;

import java.net.MalformedURLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.common.api.VersionApi;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminVersionApiResource {

	private final VersionApi versionApi;


	@Inject
	public AdminVersionApiResource(VersionApi versionApi) {
		this.versionApi = versionApi;
		versionApi.setEndpointUrlType("admin");
	}

	@GET
	public Response getVersions(@Context ContainerRequestContext context) throws MalformedURLException {
		return versionApi.getVersions(context);
	}

}
