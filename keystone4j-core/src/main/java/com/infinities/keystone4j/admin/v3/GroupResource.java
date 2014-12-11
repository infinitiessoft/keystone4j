package com.infinities.keystone4j.admin.v3;

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
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.GroupWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.utils.Views;

//keystone.identity.routers 20141211

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
	public Response createGroup(GroupWrapper groupWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(groupController.createGroup(groupWrapper.getGroup())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Group> listGroups(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return groupController.listGroups();
	}

	@GET
	@Path("/{groupid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Group> getGroup(@PathParam("groupid") String groupid) throws Exception {
		return groupController.getGroup(groupid);
	}

	@PATCH
	@Path("/{groupid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Group> updateGroup(@PathParam("groupid") String groupid, GroupWrapper groupWrapper)
			throws Exception {
		return groupController.updateGroup(groupid, groupWrapper.getGroup());
	}

	@DELETE
	@Path("/{groupid}")
	public Response deleteGroup(@PathParam("groupid") String groupid) throws Exception {
		groupController.deleteGroup(groupid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{groupid}/users")
	@JsonView(Views.Basic.class)
	public CollectionWrapper<User> listUsersInGroup(@PathParam("groupid") String groupid,
			@QueryParam("domain_id") String domainid, @QueryParam("email") String email,
			@QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return userController.listUsersInGroup(groupid);
	}

	@PUT
	@Path("/{groupid}/users/{userid}")
	@JsonView(Views.Basic.class)
	public Response addUserToGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid)
			throws Exception {
		userController.addUserToGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{groupid}/users/{userid}")
	public Response checkUserInGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid)
			throws Exception {
		userController.checkUserInGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{groupid}/users/{userid}")
	public Response removeUserFromGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid)
			throws Exception {
		userController.removeUserFromGroup(groupid, userid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}
}
