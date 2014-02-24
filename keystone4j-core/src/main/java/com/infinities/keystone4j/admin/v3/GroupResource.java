package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.GroupWrapper;
import com.infinities.keystone4j.identity.model.GroupsWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;

public class GroupResource {

	private UserV3Controller userController;
	private GroupV3Controller groupController;


	@POST
	public GroupWrapper createGroup(Group group) {
		return groupController.createGroup(group);
	}

	@GET
	public GroupsWrapper listGroups(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return groupController.listGroups(domainid, name, page, perPage);
	}

	@GET
	@Path("/{groupid}")
	public GroupWrapper getGroup(@PathParam("groupid") String groupid) {
		return groupController.getGroup(groupid);
	}

	@PATCH
	@Path("/{groupid}")
	public GroupWrapper updateGroup(@PathParam("groupid") String groupid, Group group) {
		return groupController.updateGroup(groupid, group);
	}

	@DELETE
	@Path("/{groupid}")
	public Response deleteGroup(@PathParam("groupid") String groupid) {
		groupController.deleteGroup(groupid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{groupid}/users")
	public UsersWrapper listUsersInGroup(@PathParam("groupid") String groupid, @QueryParam("domain_id") String domainid,
			@QueryParam("email") String email, @QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return userController.listUsersInGroup(groupid, domainid, email, enabled, name, page, perPage);
	}

	@PUT
	@Path("/{groupid}/users/{userid}")
	public Response listUsersInGroup(@PathParam("groupid") String groupid, @PathParam("userid") String userid) {
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
