package com.infinities.keystone4j.main;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.admin.v3.ApiV3Resource;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class PublicResource {

	@Path("/v3")
	public Class<ApiV3Resource> getApiV3Resource() {
		return ApiV3Resource.class;
	}

	@Path("/")
	public Class<PublicVersionApiResource> getPublicVersionApiResource() {
		return PublicVersionApiResource.class;
	}

}
