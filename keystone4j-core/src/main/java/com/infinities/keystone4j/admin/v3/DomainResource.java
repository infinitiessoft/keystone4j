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
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.assignment.wrapper.DomainWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.DomainsWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.model.utils.Views;

//keystone.assignment.routers 20141209

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DomainResource {

	private final RoleV3Controller roleController;
	private final DomainV3Controller domainController;


	@Inject
	public DomainResource(RoleV3Controller roleController, DomainV3Controller domainController) {
		this.roleController = roleController;
		this.domainController = domainController;
	}

	@POST
	@JsonView(Views.Advance.class)
	public Response createDomain(DomainWrapper domainWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(domainController.createDomain(domainWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public DomainsWrapper listDomain(@QueryParam("name") String name, @QueryParam("enabled") Boolean enabled,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (DomainsWrapper) domainController.listDomains();
	}

	@GET
	@Path("/{domainid}")
	@JsonView(Views.Advance.class)
	public DomainWrapper getDomain(@PathParam("domainid") String domainid) throws Exception {
		return (DomainWrapper) domainController.getDomain(domainid);
	}

	@PATCH
	@Path("/{domainid}")
	@JsonView(Views.Advance.class)
	public DomainWrapper updateDomain(@PathParam("domainid") String domainid, DomainWrapper domainWrapper) throws Exception {
		return (DomainWrapper) domainController.updateDomain(domainid, domainWrapper.getRef());
	}

	@DELETE
	@Path("/{domainid}")
	public Response deleteDomain(@PathParam("domainid") String domainid) throws Exception {
		domainController.deleteDomain(domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{domainid}/users/{userid}/roles")
	@JsonView(Views.Advance.class)
	public RolesWrapper listGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (RolesWrapper) roleController.listGrants(userid, null, domainid, null);
	}

	@GET
	@Path("/{domainid}/groups/{groupid}/roles")
	@JsonView(Views.Advance.class)
	public RolesWrapper listGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (RolesWrapper) roleController.listGrants(null, groupid, domainid, null);
	}

	@PUT
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response createGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response createGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response checkGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response checkGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response revokeGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, userid, null, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response revokeGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, null, groupid, domainid, null);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
