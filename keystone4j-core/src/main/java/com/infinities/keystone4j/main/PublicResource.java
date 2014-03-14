package com.infinities.keystone4j.main;

import javax.ws.rs.Path;

import com.infinities.keystone4j.admin.v3.ApiV3Resource;

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
