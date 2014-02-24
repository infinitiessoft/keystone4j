package com.infinities.keystone4j.policy;

import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public interface BaseCheck {

	String getRule();

	boolean check(Target target, Token token, Enforcer enforcer);

}
