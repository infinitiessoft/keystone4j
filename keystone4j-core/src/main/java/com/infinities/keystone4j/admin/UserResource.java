package com.infinities.keystone4j.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.infinities.keystone4j.assignment.controller.RoleController;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.identity.controller.UserController;
import com.infinities.keystone4j.identity.model.UserWrapper;

public class UserResource {

	private UserController userController;
	private RoleController roleController;


	public UserResource() {
		// this.identityApi = new IdentityApi();
	}

	// @GET
	// @Path("/")
	// public ExtensionsWrapper listUsers() {
	// return extensionApi.getExtensionsInfo();
	// }

	@GET
	@Path("/{userid}")
	public UserWrapper getUser(@PathParam("userid") String userid) {
		return userController.getUser(userid);
	}

	// from assignment, seems be depreciated
	@GET
	@Path("/{userid}/roles")
	public RolesWrapper getUserRoles(@PathParam("userid") String userid) {
		return roleController.getUserRoles(userid, null);
	}

}
