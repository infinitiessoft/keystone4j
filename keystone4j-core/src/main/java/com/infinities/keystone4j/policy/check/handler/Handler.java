package com.infinities.keystone4j.policy.check.handler;

import com.infinities.keystone4j.policy.model.Target;

public interface Handler {

	public boolean handle(String match, String kind, Target target);

}
