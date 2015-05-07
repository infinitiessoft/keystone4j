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
package com.infinities.keystone4j.admin;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.admin.v2.ApiV2Resource;
import com.infinities.keystone4j.admin.v3.ApiV3Resource;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class AdminResource {

	@Path("/v3")
	public Class<ApiV3Resource> getApiV3Resource() {
		return ApiV3Resource.class;
	}

	@Path("/v2.0")
	public Class<ApiV2Resource> getApiV2Resource() {
		return ApiV2Resource.class;
	}

	@Path("/")
	public Class<AdminVersionApiResource> getPublicVersionApiResource() {
		return AdminVersionApiResource.class;
	}

}
