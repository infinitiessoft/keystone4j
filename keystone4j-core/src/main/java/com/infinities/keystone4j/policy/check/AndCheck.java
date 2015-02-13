package com.infinities.keystone4j.policy.check;

import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class AndCheck implements BaseCheck {

	private final List<BaseCheck> rules;


	public AndCheck(List<BaseCheck> rules) {
		this.rules = rules;
	}

	@Override
	public String getRule() {
		return "and";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {

		for (BaseCheck rule : rules) {
			if (!rule.check(target, creds, enforcer)) {
				return false;
			}
		}
		return true;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}

	@Override
	public String toString() {
		String join = Joiner.on(" and ").join(rules);
		return String.format("(%s)", join);
	}
}
