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
		roleController.createGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response createGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByUserDomain(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public CollectionWrapper<Role> listGrantByUserDomain(@PathParam("domainid") String domainid,
			@PathParam("userid") String userid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrantsByUserDomain(userid, domainid);
	}

	@GET
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public CollectionWrapper<Role> listGrantByGroupDomain(@PathParam("domainid") String domainid,
			@PathParam("groupid") String groupid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrantsByGroupDomain(groupid, domainid);
	}

	@DELETE
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByUserDomain(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByGroupDomain(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
