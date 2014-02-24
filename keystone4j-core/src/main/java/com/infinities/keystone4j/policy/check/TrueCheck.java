package com.infinities.keystone4j.policy.check;

import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class TrueCheck implements BaseCheck {

	@Override
	public String getRule() {
		return "@";
	}

	@Override
	public boolean check(Target target, Token token, Enforcer enforcer) {
		return true;
	}

}
