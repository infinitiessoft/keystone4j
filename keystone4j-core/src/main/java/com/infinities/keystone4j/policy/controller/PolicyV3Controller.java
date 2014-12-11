package com.infinities.keystone4j.policy.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;

public interface PolicyV3Controller {

	MemberWrapper<Policy> createPolicy(Policy policy) throws Exception;

	CollectionWrapper<Policy> listPolicies() throws Exception;

	MemberWrapper<Policy> getPolicy(String policyid) throws Exception;

	MemberWrapper<Policy> updatePolicy(String policyid, Policy policy) throws Exception;

	void deletePolicy(String policyid) throws Exception;

}
