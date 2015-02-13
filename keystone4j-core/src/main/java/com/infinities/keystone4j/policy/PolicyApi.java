package com.infinities.keystone4j.policy;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;

public interface PolicyApi extends Api {

	Policy createPolicy(String policyid, Policy policy) throws Exception;

	List<Policy> listPolicies(Hints hints) throws Exception;

	Policy getPolicy(String policyid) throws Exception;

	Policy updatePolicy(String policyid, Policy policy) throws Exception;

	Policy deletePolicy(String policyid) throws Exception;

	// doRaise=true
	void enforce(Context creds, String action, Map<String, Object> target) throws Exception;

}
