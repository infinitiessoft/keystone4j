package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class TrueCheck implements BaseCheck {

	@Override
	public String getRule() {
		return "@";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		return true;
	}

	@Override
	public String toString() {
		return "@";
	}

}
