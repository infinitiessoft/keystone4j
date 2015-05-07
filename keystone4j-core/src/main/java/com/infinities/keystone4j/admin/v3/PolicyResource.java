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
import javax.ws.rs.POST;
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
import com.infinities.keystone4j.model.policy.wrapper.PoliciesWrapper;
import com.infinities.keystone4j.model.policy.wrapper.PolicyWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;

//keystone.policy.routers 20141211

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PolicyResource {

	private final PolicyV3Controller policyController;


	@Inject
	public PolicyResource(PolicyV3Controller policyController) {
		this.policyController = policyController;
	}

	@POST
	@JsonView(Views.Advance.class)
	public Response createPolicy(PolicyWrapper wrapper) throws Exception {
		return Response.status(Status.CREATED).entity(policyController.createPolicy(wrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public PoliciesWrapper listPolicys(@QueryParam("type") String type, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (PoliciesWrapper) policyController.listPolicies();
	}

	@GET
	@Path("/{policyid}")
	@JsonView(Views.Advance.class)
	public PolicyWrapper getPolicy(@PathParam("policyid") String policyid) throws Exception {
		return (PolicyWrapper) policyController.getPolicy(policyid);
	}

	@PATCH
	@Path("/{policyid}")
	@JsonView(Views.Advance.class)
	public PolicyWrapper updatePolicy(@PathParam("policyid") String policyid, PolicyWrapper wrapper) throws Exception {
		return (PolicyWrapper) policyController.updatePolicy(policyid, wrapper.getRef());
	}

	@DELETE
	@Path("/{policyid}")
	public Response deletePolicy(@PathParam("policyid") String policyid) throws Exception {
		policyController.deletePolicy(policyid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
