package com.infinities.keystone4j.policy.check;

import java.util.List;

import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

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
	public boolean check(Target target, Token token, Enforcer enforcer) {

		for (BaseCheck rule : rules) {
			if (!rule.check(target, token, enforcer)) {
				return false;
			}
		}
		return true;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}
}
