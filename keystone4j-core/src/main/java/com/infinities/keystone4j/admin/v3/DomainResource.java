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
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.DomainWrapper;
import com.infinities.keystone4j.assignment.model.DomainsWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class DomainResource {

	private RoleV3Controller roleController;
	private DomainV3Controller domainController;


	public DomainResource() {
		// this.identityApi = new IdentityApi();
	}

	@POST
	public DomainWrapper createDomain(Domain domain) {
		return domainController.createDomain(domain);
	}

	@GET
	public DomainsWrapper listDomain(@QueryParam("name") String name, @QueryParam("enabled") Boolean enabled,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return domainController.listDomains(name, enabled, page, perPage);
	}

	@GET
	@Path("/{domainid}")
	public DomainWrapper getDomain(@PathParam("domainid") String domainid) {
		return domainController.getDomain(domainid);
	}

	@PATCH
	@Path("/{domainid}")
	public DomainWrapper updateDomain(@PathParam("domainid") String domainid, Domain domain) {
		return domainController.updateDomain(domainid, domain);
	}

	@DELETE
	@Path("/{domainid}")
	public Response deleteDomain(@PathParam("domainid") String domainid) {
		domainController.deleteDomain(domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response createGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response createGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response checkGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response checkGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public RolesWrapper listGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByUserDomain(userid, domainid, page, perPage);
	}

	@GET
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public RolesWrapper listGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByGroupDomain(groupid, domainid, page, perPage);
	}

	@DELETE
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response revokeGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response revokeGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
