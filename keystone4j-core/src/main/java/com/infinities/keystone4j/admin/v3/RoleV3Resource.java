package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class RoleV3Resource {

	private RoleV3Controller roleController;


	@POST
	public RoleWrapper createRole(Role role) {
		return roleController.createRole(role);
	}

	@GET
	public RolesWrapper listRoles(@QueryParam("name") String name, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listRoles(name, page, perPage);
	}

	@GET
	@Path("/{roleid}")
	public RoleWrapper getRole(@PathParam("roleid") String roleid) {
		return roleController.getRole(roleid);
	}

	@PATCH
	@Path("/{roleid}")
	public RoleWrapper updateRole(@PathParam("roleid") String roleid, Role role) {
		return roleController.updateRole(roleid, role);
	}

	@DELETE
	@Path("/{roleid}")
	public Response deleteRole(@PathParam("roleid") String roleid) {
		roleController.deleteRole(roleid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
