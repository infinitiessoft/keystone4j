package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

public class NotCheck implements BaseCheck {

	private final BaseCheck rule;


	public NotCheck(BaseCheck rule) {
		this.rule = rule;
	}

	@Override
	public String getRule() {
		return "not";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {
		return !rule.check(target, token, parMap, enforcer);
	}
}
