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
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.DomainWrapper;
import com.infinities.keystone4j.model.assignment.Role;
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
	@JsonView(Views.Basic.class)
	public Response createDomain(DomainWrapper domainWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(domainController.createDomain(domainWrapper.getDomain())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Domain> listDomain(@QueryParam("name") String name, @QueryParam("enabled") Boolean enabled,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return domainController.listDomains();
	}

	@GET
	@Path("/{domainid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Domain> getDomain(@PathParam("domainid") String domainid) throws Exception {
		return domainController.getDomain(domainid);
	}

	@PATCH
	@Path("/{domainid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Domain> updateDomain(@PathParam("domainid") String domainid, DomainWrapper domainWrapper)
			throws Exception {
		return domainController.updateDomain(domainid, domainWrapper.getDomain());
	}

	@DELETE
	@Path("/{domainid}")
	public Response deleteDomain(@PathParam("domainid") String domainid) throws Exception {
		domainController.deleteDomain(domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response createGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response createGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response checkGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response checkGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{domainid}/users/{userid}/roles")
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Role> listGrantByUser(@PathParam("domainid") String domainid,
			@PathParam("userid") String userid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrantsByUserDomain(userid, domainid);
	}

	@GET
	@Path("/{domainid}/groups/{groupid}/roles")
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Role> listGrantByGroup(@PathParam("domainid") String domainid,
			@PathParam("groupid") String groupid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return roleController.listGrantsByGroupDomain(groupid, domainid);
	}

	@DELETE
	@Path("/{domainid}/users/{userid}/roles/{roleid}")
	public Response revokeGrantByUser(@PathParam("domainid") String domainid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrantByUserDomain(roleid, userid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{domainid}/groups/{groupid}/roles/{roleid}")
	public Response revokeGrantByGroup(@PathParam("domainid") String domainid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrantByGroupDomain(roleid, groupid, domainid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
