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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;

//keystone.assignment.routers 20141210

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OSInheritResource {

	private final RoleV3Controller roleController;


	@Inject
	public OSInheritResource(RoleV3Controller roleController) {
		this.roleController = roleController;
	}

	@PUT
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response createGrantByUserDomain(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response createGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByUserDomain(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public CollectionWrapper<Role> listGrantByUserDomain(@PathParam("domainid") String domainid,
			@PathParam("userid") String userid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrants(userid, null, domainid, null);
	}

	@GET
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public CollectionWrapper<Role> listGrantByGroupDomain(@PathParam("domainid") String domainid,
			@PathParam("groupid") String groupid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrants(null, groupid, domainid, null);
	}

	@DELETE
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByUserDomain(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
