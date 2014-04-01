package com.infinities.keystone4j.admin.v3;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.GroupWrapper;
import com.infinities.keystone4j.identity.model.GroupsWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;
import com.infinities.keystone4j.utils.jackson.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {

	private final UserV3Controller userController;
	private final GroupV3Controller groupController;


	@Inject
	public GroupResource(UserV3Controller userController, GroupV3Controller groupController) {
		this.userController = userController;
		this.groupController = groupController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createGroup(GroupWrapper groupWrapper) {
		return Response.status(Status.CREATED).entity(groupController.createGroup(groupWrapper.getGroup())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public GroupsWrapper listGroups(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return groupController.listGroups(domainid, name, page, perPage);
	}

	@GET
	@Path("/{groupid}")
	@JsonView(Views.Basic.class)
	public GroupWrapper getGroup(@PathParam("groupid") String groupid) {
		return groupController.getGroup(groupid);
	}

	@PATCH
	@Path("/{groupid}")
	@JsonView(Views.Basic.class)
	public GroupWrapper updateGroup(@PathParam("groupid") String groupid, GroupWrapper groupWrapper) {
		return groupController.updateGroup(groupid, groupWrapper.getGroup());
	}

	@DELETE
	@Path("/{groupid}")
	public Response deleteGroup(@PathParam("groupid") String groupid) {
		groupController.deleteGroup(groupid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{groupid}/users")
	@JsonView(Views.Basic.class)
	public UsersWrapper listUsersInGroup(@PathParam("groupid") String groupid, @QueryParam("domain_id") String domainid,
			@QueryParam("email") String email, @QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws JsonGenerationException, JsonMappingException, IOException {
		return userController.listUsersInGroup(groupid, domainid, email, enabled, name, page, perPage);
	}

	@PUT
	@Path("/{groupid}/users/{userid}")
	@JsonView(Views.Basic.class)
	public Response addUserToGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {
		userController.addUserToGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{groupid}/users/{userid}")
	public Response checkUserInGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {
		userController.checkUserInGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{groupid}/users/{userid}")
	public Response removeUserFromGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {
		userController.removeUserFromGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}
}
