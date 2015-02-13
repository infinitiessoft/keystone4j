package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

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
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		return !rule.check(target, creds, enforcer);
	}

	@Override
	public String toString() {
		return String.format("not %s", rule);
	}
}
