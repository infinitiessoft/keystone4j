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

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.model.utils.Views;

//keystone.assignment.routers 20141209

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleV3Resource {

	private final RoleV3Controller roleController;


	@Inject
	public RoleV3Resource(RoleV3Controller roleController) {
		this.roleController = roleController;
	}

	@POST
	@JsonView(Views.Advance.class)
	public Response createRole(RoleWrapper roleWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(roleController.createRole(roleWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public RolesWrapper listRoles(@QueryParam("name") String name, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (RolesWrapper) roleController.listRoles();
	}

	@GET
	@Path("/{roleid}")
	@JsonView(Views.Advance.class)
	public RoleWrapper getRole(@PathParam("roleid") String roleid) throws Exception {
		return (RoleWrapper) roleController.getRole(roleid);
	}

	@PATCH
	@Path("/{roleid}")
	@JsonView(Views.Advance.class)
	public RoleWrapper updateRole(@PathParam("roleid") String roleid, RoleWrapper roleWrapper) throws Exception {
		return (RoleWrapper) roleController.updateRole(roleid, roleWrapper.getRef());
	}

	@DELETE
	@Path("/{roleid}")
	public Response deleteRole(@PathParam("roleid") String roleid) throws Exception {
		roleController.deleteRole(roleid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
