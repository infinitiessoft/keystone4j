package com.infinities.keystone4j.admin;

import javax.ws.rs.Path;

import com.infinities.keystone4j.admin.v3.ApiV3Resource;

@Path("/")
public class AdminResource {

	@Path("/v2.0")
	public Class<AdminApiV2Resource> getAdminApiV2Resource() {
		return AdminApiV2Resource.class;
	}

	@Path("/v3")
	public Class<ApiV3Resource> getAdminApiV3Resource() {
		return ApiV3Resource.class;
	}

	@Path("/")
	public Class<AdminVersionApiResource> getAdminVersionApiResource() {
		return AdminVersionApiResource.class;
	}
}
