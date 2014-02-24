package com.infinities.keystone4j.policy.check;

import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class RuleCheck extends Check {

	@Override
	public String getRule() {
		return "rule";
	}

	@Override
	public boolean check(Target target, Token token, Enforcer enforcer) {
		try {
			return enforcer.getRules().get(this.getMatch()).check(target, token, enforcer);
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
