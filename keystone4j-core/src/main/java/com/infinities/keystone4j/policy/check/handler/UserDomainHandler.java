package com.infinities.keystone4j.policy.check.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.policy.model.Target;

public class UserDomainHandler extends AbstractHandler {

	private final Logger logger = LoggerFactory.getLogger(UserDomainHandler.class);


	public UserDomainHandler(AbstractHandler next) {
		super(next);
	}

	@Override
	protected String getMatch() {
		return "target.user.domain_id";
	}

	@Override
	protected boolean isMatch(Target target, String kind) {
		try {
			if (target.getUser().getDomain().getId().equals(kind)) {
				return true;
			}
		} catch (Exception e) {
			logger.warn("policy match failed", e);
		}
		return false;
	}
}
