package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignmentsWrapper;

public class RoleAssignmentV3Resource {

	private final RoleAssignmentV3Controller roleAssignmentController;


	public RoleAssignmentV3Resource(RoleAssignmentV3Controller roleAssignmentController) {
		this.roleAssignmentController = roleAssignmentController;
	}

	@POST
	public Response createRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@GET
	public RoleAssignmentsWrapper listRoleAssignment() {
		// TODO not implemented yet
		roleAssignmentController.listRoleAssignments();
		throw Exceptions.NotImplementedException.getInstance();
	}

	@GET
	@Path("/{roleAssignmentid}")
	public RoleAssignmentWrapper getRoleAssignment(@PathParam("roleAssignmentid") String roleAssignmentid) {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@PATCH
	@Path("/{roleAssignmentid}")
	public RoleAssignmentWrapper updateRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@DELETE
	@Path("/{roleAssignmentid}")
	public Response deleteRoleAssignment(@PathParam("roleAssignmentid") String roleAssignmentid) {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

}
