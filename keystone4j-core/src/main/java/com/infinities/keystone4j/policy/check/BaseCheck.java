package com.infinities.keystone4j.policy.check;

import java.util.Map;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public interface BaseCheck {

	String getRule();

	boolean check(Map<String, Object> target, Context creds, Enforcer enforcer);

}
