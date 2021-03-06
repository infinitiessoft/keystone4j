/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
import com.infinities.keystone4j.model.identity.wrapper.GroupWrapper;
import com.infinities.keystone4j.model.identity.wrapper.GroupsWrapper;
import com.infinities.keystone4j.model.identity.wrapper.UsersWrapper;
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
	@JsonView(Views.Advance.class)
	public Response createGroup(GroupWrapper groupWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(groupController.createGroup(groupWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public GroupsWrapper listGroups(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (GroupsWrapper) groupController.listGroups();
	}

	@GET
	@Path("/{groupid}")
	@JsonView(Views.Advance.class)
	public GroupWrapper getGroup(@PathParam("groupid") String groupid) throws Exception {
		return (GroupWrapper) groupController.getGroup(groupid);
	}

	@PATCH
	@Path("/{groupid}")
	@JsonView(Views.Advance.class)
	public GroupWrapper updateGroup(@PathParam("groupid") String groupid, GroupWrapper groupWrapper) throws Exception {
		return (GroupWrapper) groupController.updateGroup(groupid, groupWrapper.getRef());
	}

	@DELETE
	@Path("/{groupid}")
	public Response deleteGroup(@PathParam("groupid") String groupid) throws Exception {
		groupController.deleteGroup(groupid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{groupid}/users")
	@JsonView(Views.Advance.class)
	public UsersWrapper listUsersInGroup(@PathParam("groupid") String groupid, @QueryParam("domain_id") String domainid,
			@QueryParam("email") String email, @QueryParam("enabled") Boolean enabled, @QueryParam("name") String name,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (UsersWrapper) userController.listUsersInGroup(groupid);
	}

	@PUT
	@Path("/{groupid}/users/{userid}")
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
