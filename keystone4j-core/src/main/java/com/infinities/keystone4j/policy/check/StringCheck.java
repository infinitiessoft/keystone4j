package com.infinities.keystone4j.policy.check;

import com.infinities.keystone4j.policy.BaseCheck;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class StringCheck implements BaseCheck {

	private final String sequence;


	public StringCheck(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String getRule() {
		return sequence;
	}

	@Override
	public boolean check(Target target, Token token, Enforcer enforcer) {
		return false;
	}

}
