package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

public class RuleCheck extends Check {

	@Override
	public String getRule() {
		return "rule";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {
		try {
			return enforcer.getRules().get(this.getMatch()).check(target, token, parMap, enforcer);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new RuleCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
