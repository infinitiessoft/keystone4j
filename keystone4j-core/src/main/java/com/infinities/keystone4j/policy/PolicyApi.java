package com.infinities.keystone4j.policy;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.TargetWrapper;

public interface PolicyApi extends Api {

	Policy createPolicy(Policy policy);

	List<Policy> listPolicies(Hints hints);

	Policy getPolicy(String policyid);

	Policy updatePolicy(String policyid, Policy policy);

	Policy deletePolicy(String policyid);

	void enforce(AuthContext creds, String action, TargetWrapper target, boolean doRaise);

}
