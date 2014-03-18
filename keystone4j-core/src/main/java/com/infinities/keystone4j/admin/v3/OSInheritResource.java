package com.infinities.keystone4j.admin.v3;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class OSInheritResource {

	private final RoleV3Controller roleController;


	public OSInheritResource(RoleV3Controller roleController) {
		this.roleController = roleController;
	}

	@PUT
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response createGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response createGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response checkGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public List<Role> listGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByUserDomain(userid, domainid, page, perPage);
	}

	@GET
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public List<Role> listGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByGroupDomain(groupid, domainid, page, perPage);
	}

	@DELETE
	@Path("/domains/{domainid}/users/{userid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/domains/{domainid}/groups/{groupid}/roles/{roleid}/inherited_to_projects")
	public Response revokeGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
