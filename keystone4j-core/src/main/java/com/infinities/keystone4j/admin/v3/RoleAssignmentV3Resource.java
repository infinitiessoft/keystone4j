package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.assignment.model.RoleAssignmentWrapper;
import com.infinities.keystone4j.assignment.model.RoleAssignmentsWrapper;
import com.infinities.keystone4j.exception.NotImplementedException;

public class RoleAssignmentV3Resource {

	private RoleAssignmentV3Controller roleAssignmentController;


	@POST
	public Response createRoleAssignment() {
		// TODO not implemented yet
		throw new NotImplementedException(null, null);
	}

	@GET
	public RoleAssignmentsWrapper listRoleAssignment() {
		// TODO not implemented yet
		roleAssignmentController.listRoleAssignments();
		throw new NotImplementedException(null, null);
	}

	@GET
	@Path("/{roleAssignmentid}")
	public RoleAssignmentWrapper getRoleAssignment(@PathParam("roleAssignmentid") String roleAssignmentid) {
		// TODO not implemented yet
		throw new NotImplementedException(null, null);
	}

	@PATCH
	@Path("/{roleAssignmentid}")
	public RoleAssignmentWrapper updateRoleAssignment() {
		// TODO not implemented yet
		throw new NotImplementedException(null, null);
	}

	@DELETE
	@Path("/{roleAssignmentid}")
	public Response deleteRoleAssignment(@PathParam("roleAssignmentid") String roleAssignmentid) {
		// TODO not implemented yet
		throw new NotImplementedException(null, null);
	}

}
