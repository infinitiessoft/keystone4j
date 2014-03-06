package com.infinities.keystone4j.policy;

import java.util.Map;

import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

public interface BaseCheck {

	String getRule();

	boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer);

}
