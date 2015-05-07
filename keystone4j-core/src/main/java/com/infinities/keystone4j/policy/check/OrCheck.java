package com.infinities.keystone4j.policy.check;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class OrCheck implements BaseCheck {

	private final List<BaseCheck> rules;
	private final Logger logger = LoggerFactory.getLogger(OrCheck.class);


	public OrCheck(List<BaseCheck> rules) {
		this.rules = rules;
	}

	@Override
	public String getRule() {
		return "or";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		logger.debug("rules size: {}", rules.size());
		for (BaseCheck rule : rules) {
			logger.debug("rules: {}", rule.getRule());
			if (rule.check(target, creds, enforcer)) {
				return true;
			}
		}
		return false;
	}

	public void addCheck(BaseCheck check) {
		this.rules.add(check);
	}

	@Override
	public String toString() {
		String join = Joiner.on(" or ").join(rules);
		return String.format("(%s)", join);
	}
}
