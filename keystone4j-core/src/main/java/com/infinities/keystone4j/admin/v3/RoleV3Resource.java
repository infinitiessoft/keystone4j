package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.map.annotate.JsonView;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class RoleV3Resource {

	private final RoleV3Controller roleController;


	@Inject
	public RoleV3Resource(RoleV3Controller roleController) {
		this.roleController = roleController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createRole(RoleWrapper roleWrapper) {
		return Response.status(Status.CREATED).entity(roleController.createRole(roleWrapper.getRole())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public RolesWrapper listRoles(@QueryParam("name") String name, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listRoles(name, page, perPage);
	}

	@GET
	@Path("/{roleid}")
	@JsonView(Views.Basic.class)
	public RoleWrapper getRole(@PathParam("roleid") String roleid) {
		return roleController.getRole(roleid);
	}

	@PATCH
	@Path("/{roleid}")
	@JsonView(Views.Basic.class)
	public RoleWrapper updateRole(@PathParam("roleid") String roleid, RoleWrapper roleWrapper) {
		return roleController.updateRole(roleid, roleWrapper.getRole());
	}

	@DELETE
	@Path("/{roleid}")
	public Response deleteRole(@PathParam("roleid") String roleid) {
		roleController.deleteRole(roleid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
