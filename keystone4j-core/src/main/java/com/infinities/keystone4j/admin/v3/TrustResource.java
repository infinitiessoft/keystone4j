package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.assignment.model.RoleWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustWrapper;
import com.infinities.keystone4j.trust.model.TrustsWrapper;

public class TrustResource {

	private final TrustV3Controller trustController;


	public TrustResource(TrustV3Controller trustController) {
		this.trustController = trustController;
	}

	@POST
	public TrustWrapper createTrust(Trust trust) {
		return trustController.createTrust(trust);
	}

	@GET
	public TrustsWrapper listTrusts(@QueryParam("trustor_id") String trustorid, @QueryParam("trustee_id") String trusteeid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return trustController.listTrusts(trustorid, trusteeid, page, perPage);
	}

	@GET
	@Path("/{trustid}")
	public TrustWrapper getTrust(@PathParam("trustid") String trustid) {
		return trustController.getTrust(trustid);
	}

	@DELETE
	@Path("/{trustid}")
	public Response deleteTrust(@PathParam("trustid") String trustid) {
		trustController.deleteTrust(trustid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{trustid}/roles")
	public RolesWrapper listRolesForTrust(@PathParam("trustid") String trustid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return trustController.listRolesForTrust(trustid, page, perPage);
	}

	@HEAD
	@Path("/{trustid}/roles/{roleid}")
	public Response checkRoleForTrust(@PathParam("trustid") String trustid, @PathParam("roleid") String roleid) {
		trustController.checkRoleForTrust(trustid, roleid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{trustid}/roles/{roleid}")
	public RoleWrapper getRoleForTrust(@PathParam("trustid") String trustid, @PathParam("roleid") String roleid) {
		return trustController.getRoleForTrust(trustid, roleid);
	}
}
