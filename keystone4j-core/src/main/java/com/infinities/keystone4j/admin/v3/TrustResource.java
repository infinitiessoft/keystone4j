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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TrustResource {

	private final TrustV3Controller trustController;


	public TrustResource(TrustV3Controller trustController) {
		this.trustController = trustController;
	}

	@POST
	public MemberWrapper<Trust> createTrust(Trust trust) throws Exception {
		return trustController.createTrust(trust);
	}

	@GET
	public CollectionWrapper<Trust> listTrusts(@QueryParam("trustor_id") String trustorid,
			@QueryParam("trustee_id") String trusteeid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return trustController.listTrusts();
	}

	@GET
	@Path("/{trustid}")
	public MemberWrapper<Trust> getTrust(@PathParam("trustid") String trustid) throws Exception {
		return trustController.getTrust(trustid);
	}

	@DELETE
	@Path("/{trustid}")
	public Response deleteTrust(@PathParam("trustid") String trustid) throws Exception {
		trustController.deleteTrust(trustid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{trustid}/roles")
	public CollectionWrapper<Role> listRolesForTrust(@PathParam("trustid") String trustid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return trustController.listRolesForTrust(trustid);
	}

	@HEAD
	@Path("/{trustid}/roles/{roleid}")
	public Response checkRoleForTrust(@PathParam("trustid") String trustid, @PathParam("roleid") String roleid)
			throws Exception {
		trustController.checkRoleForTrust(trustid, roleid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{trustid}/roles/{roleid}")
	public MemberWrapper<Role> getRoleForTrust(@PathParam("trustid") String trustid, @PathParam("roleid") String roleid)
			throws Exception {
		return trustController.getRoleForTrust(trustid, roleid);
	}
}
