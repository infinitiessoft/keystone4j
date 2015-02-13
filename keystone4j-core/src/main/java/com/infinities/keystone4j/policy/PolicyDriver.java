package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;

public interface PolicyDriver extends Driver {

	Policy createPolicy(String policyid, Policy policy);

	List<Policy> listPolicies();

	Policy getPolicy(String policyid);

	Policy updatePolicy(String policyid, Policy policy);

	void deletePolicy(String policyid);

	Policy enforce(Context context, String action, Map<String, Object> target) throws Exception;

}
