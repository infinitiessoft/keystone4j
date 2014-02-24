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
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.GroupsWrapper;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;
import com.infinities.keystone4j.identity.model.UserWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;

public class UserV3Resource {

	private ProjectV3Controller projectController;
	private UserV3Controller userController;
	private GroupV3Controller groupController;


	public UserV3Resource() {
		// this.identityApi = new IdentityApi();
	}

	@GET
	@Path("/{userid}/projects")
	public ProjectsWrapper listUserProjects(@PathParam("userid") String userid, @QueryParam("name") String name,
			@QueryParam("enabled") Boolean enabled, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return projectController.listUserProjects(userid, enabled, name, page, perPage);
	}

	@POST
	public UserWrapper createUser(User user) {
		return userController.createUser(user);
	}

	@GET
	public UsersWrapper listUsers(@QueryParam("domain_id") String domainid, @QueryParam("email") String email,
			@QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return userController.listUsers(domainid, email, enabled, name, page, perPage);
	}

	@GET
	@Path("/{userid}")
	public UserWrapper getUser(@PathParam("userid") String userid) {
		return userController.getUser(userid);
	}

	@PATCH
	@Path("/{userid}")
	public UserWrapper updateUser(@PathParam("userid") String userid, User user) {
		return userController.updateUser(userid, user);
	}

	@DELETE
	@Path("/{userid}")
	public Response deleteUser(@PathParam("userid") String userid) {
		userController.deleteUser(userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@POST
	@Path("/{userid}/password")
	public Response changePassword(@PathParam("userid") String userid, UserParam user) {
		userController.changePassword(userid, user);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{userid}/groups")
	public GroupsWrapper listGroupsForUser(@PathParam("userid") String userid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return groupController.listGroupsForUser(userid, name, page, perPage);
	}

}
