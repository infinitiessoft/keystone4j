package com.infinities.keystone4j.policy.controller;

import com.infinities.keystone4j.policy.model.PoliciesWrapper;
import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.PolicyWrapper;

public interface PolicyV3Controller {

	PolicyWrapper createPolicy(Policy policy);

	PoliciesWrapper listPolicies(String type, int page, int perPage);

	PolicyWrapper getPolicy(String policyid);

	PolicyWrapper updatePolicy(String policyid, Policy policy);

	void deletePolicy(String policyid);

}
