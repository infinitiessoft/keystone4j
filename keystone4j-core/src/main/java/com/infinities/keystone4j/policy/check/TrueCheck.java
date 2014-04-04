package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;

public class TrueCheck implements BaseCheck {

	@Override
	public String getRule() {
		return "@";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {
		return true;
	}

}
