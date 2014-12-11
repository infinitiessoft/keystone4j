package com.infinities.keystone4j.admin.v3;

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

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;

//keystone.policy.routers 20141211

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PolicyResource {

	private final PolicyV3Controller policyController;


	public PolicyResource(PolicyV3Controller policyController) {
		this.policyController = policyController;
	}

	@POST
	public MemberWrapper<Policy> createPolicy(Policy policy) throws Exception {
		return policyController.createPolicy(policy);
	}

	@GET
	public CollectionWrapper<Policy> listPolicys(@QueryParam("type") String type,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return policyController.listPolicies();
	}

	@GET
	@Path("/{policyid}")
	public MemberWrapper<Policy> getPolicy(@PathParam("policyid") String policyid) throws Exception {
		return policyController.getPolicy(policyid);
	}

	@PATCH
	@Path("/{policyid}")
	public MemberWrapper<Policy> updatePolicy(@PathParam("policyid") String policyid, Policy policy) throws Exception {
		return policyController.updatePolicy(policyid, policy);
	}

	@DELETE
	@Path("/{policyid}")
	public Response deletePolicy(@PathParam("policyid") String policyid) throws Exception {
		policyController.deletePolicy(policyid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
