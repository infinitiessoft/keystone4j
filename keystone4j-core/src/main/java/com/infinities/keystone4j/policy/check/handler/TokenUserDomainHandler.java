package com.infinities.keystone4j.policy.check.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.policy.model.Target;

public class TokenUserDomainHandler extends AbstractHandler {

	private final Logger logger = LoggerFactory.getLogger(TokenUserDomainHandler.class);


	public TokenUserDomainHandler(AbstractHandler next) {
		super(next);
	}

	@Override
	protected String getMatch() {
		return "target.token.user.domain.id";
	}

	@Override
	protected boolean isMatch(Target target, String kind) {
		try {
			if (target.getToken().getUser().getDomain().getId().equals(kind)) {
				return true;
			}
		} catch (Exception e) {
			logger.warn("policy match failed", e);
		}
		return false;
	}
}
