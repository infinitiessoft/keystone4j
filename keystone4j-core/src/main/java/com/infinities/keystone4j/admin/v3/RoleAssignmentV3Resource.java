package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentsWrapper;

//keystone.assignment.routers 20141210

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleAssignmentV3Resource {

	private final RoleAssignmentV3Controller roleAssignmentController;


	@Inject
	public RoleAssignmentV3Resource(RoleAssignmentV3Controller roleAssignmentController) {
		this.roleAssignmentController = roleAssignmentController;
	}

	@POST
	public Response createRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@GET
	public RoleAssignmentsWrapper listRoleAssignment() throws Exception {
		return (RoleAssignmentsWrapper) roleAssignmentController.listRoleAssignments();
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
