package com.infinities.keystone4j.policy;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public interface PolicyApi extends Api {

	Policy createPolicy(Policy policy);

	List<Policy> listPolicies();

	Policy getPolicy(String policyid);

	Policy updatePolicy(String policyid, Policy policy);

	Policy deletePolicy(String policyid);

	void enforce(Token token, String action, Target target, boolean doRaise);

}
