package com.infinities.keystone4j.policy;

import java.util.List;

import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public interface PolicyDriver {

	Policy createPolicy(Policy policy);

	List<Policy> listPolicies();

	Policy getPolicy(String policyid);

	Policy updatePolicy(String policyid, Policy policy);

	void deletePolicy(String policyid);

	Policy enforce(Token token, String action, Target target, boolean doRaise);

}
