package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;

public interface PolicyApi extends Api {

	Policy createPolicy(Policy policy);

	List<Policy> listPolicies();

	Policy getPolicy(String policyid);

	Policy updatePolicy(String policyid, Policy policy);

	Policy deletePolicy(String policyid);

	void enforce(Token token, String action, Map<String, PolicyEntity> target, Map<String, Object> parMap, boolean doRaise);

}
