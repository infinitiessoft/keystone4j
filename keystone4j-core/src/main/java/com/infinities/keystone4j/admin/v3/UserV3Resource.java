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
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.GroupsWrapper;
import com.infinities.keystone4j.identity.model.UserParamWrapper;
import com.infinities.keystone4j.identity.model.UserWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;
import com.infinities.keystone4j.utils.jackson.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserV3Resource {

	private final ProjectV3Controller projectController;
	private final UserV3Controller userController;
	private final GroupV3Controller groupController;


	@Inject
	public UserV3Resource(ProjectV3Controller projectController, UserV3Controller userController,
			GroupV3Controller groupController) {
		this.projectController = projectController;
		this.userController = userController;
		this.groupController = groupController;
	}

	@GET
	@Path("/{userid}/projects")
	@JsonView(Views.Basic.class)
	public ProjectsWrapper listUserProjects(@PathParam("userid") String userid, @QueryParam("name") String name,
			@QueryParam("enabled") Boolean enabled, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return projectController.listUserProjects(userid, enabled, name, page, perPage);
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createUser(UserWrapper userWrapper) {
		return Response.status(Status.CREATED).entity(userController.createUser(userWrapper.getUser())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public UsersWrapper listUsers(@QueryParam("domain_id") String domainid, @QueryParam("email") String email,
			@QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return userController.listUsers(domainid, email, enabled, name, page, perPage);
	}

	@GET
	@Path("/{userid}")
	@JsonView(Views.Basic.class)
	public UserWrapper getUser(@PathParam("userid") String userid) {
		return userController.getUser(userid);
	}

	@PATCH
	@Path("/{userid}")
	@JsonView(Views.Basic.class)
	public UserWrapper updateUser(@PathParam("userid") String userid, UserWrapper userWrapper) {
		return userController.updateUser(userid, userWrapper.getUser());
	}

	@DELETE
	@Path("/{userid}")
	public Response deleteUser(@PathParam("userid") String userid) {
		userController.deleteUser(userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@POST
	@Path("/{userid}/password")
	public Response changePassword(@PathParam("userid") String userid, UserParamWrapper userWrapper) {
		userController.changePassword(userid, userWrapper.getUser());
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{userid}/groups")
	@JsonView(Views.Basic.class)
	public GroupsWrapper listGroupsForUser(@PathParam("userid") String userid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return groupController.listGroupsForUser(userid, name, page, perPage);
	}

}
