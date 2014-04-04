package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.policy.PoliciesWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.PolicyWrapper;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;

public class PolicyResource {

	private final PolicyV3Controller policyController;


	public PolicyResource(PolicyV3Controller policyController) {
		this.policyController = policyController;
	}

	@POST
	public PolicyWrapper createPolicy(Policy policy) {
		return policyController.createPolicy(policy);
	}

	@GET
	public PoliciesWrapper listPolicys(@QueryParam("type") String type, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return policyController.listPolicies(type, page, perPage);
	}

	@GET
	@Path("/{policyid}")
	public PolicyWrapper getPolicy(@PathParam("policyid") String policyid) {
		return policyController.getPolicy(policyid);
	}

	@PATCH
	@Path("/{policyid}")
	public PolicyWrapper updatePolicy(@PathParam("policyid") String policyid, Policy policy) {
		return policyController.updatePolicy(policyid, policy);
	}

	@DELETE
	@Path("/{policyid}")
	public Response deletePolicy(@PathParam("policyid") String policyid) {
		policyController.deletePolicy(policyid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
