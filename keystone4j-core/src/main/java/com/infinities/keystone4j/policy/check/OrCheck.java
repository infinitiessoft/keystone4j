package com.infinities.keystone4j.policy.check;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;

public class OrCheck implements BaseCheck {

	private final List<BaseCheck> rules;


	public OrCheck(List<BaseCheck> rules) {
		this.rules = rules;
	}

	@Override
	public String getRule() {
		return "or";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {

		for (BaseCheck rule : rules) {
			if (rule.check(target, token, parMap, enforcer)) {
				return true;
			}
		}
		return false;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}
}
