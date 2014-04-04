package com.infinities.keystone4j.policy;

import java.util.Map;

import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;

public interface BaseCheck {

	String getRule();

	boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer);

}
