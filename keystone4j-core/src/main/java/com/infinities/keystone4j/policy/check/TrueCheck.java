package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

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
