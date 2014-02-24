package com.infinities.keystone4j.policy.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.check.handler.AbstractHandler;
import com.infinities.keystone4j.policy.check.handler.DomainHandler;
import com.infinities.keystone4j.policy.check.handler.GroupDomainHandler;
import com.infinities.keystone4j.policy.check.handler.ProjectDomainHandler;
import com.infinities.keystone4j.policy.check.handler.ProjectHandler;
import com.infinities.keystone4j.policy.check.handler.TokenUserDomainHandler;
import com.infinities.keystone4j.policy.check.handler.TokenUserHandler;
import com.infinities.keystone4j.policy.check.handler.UserDomainHandler;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class GenericCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(GenericCheck.class);
	private final AbstractHandler handler = new DomainHandler(new GroupDomainHandler(new ProjectDomainHandler(
			new ProjectHandler(new TokenUserDomainHandler(new TokenUserHandler(new UserDomainHandler(null)))))));


	@Override
	public String getRule() {
		return "None";
	}

	@Override
	public boolean check(Target target, Token token, Enforcer enforcer) {
		try {
			return handler.handle(this.getMatch(), this.getKind(), target);
		} catch (Exception e) {
			logger.warn("check failed", e);
			return false;
		}
	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new GenericCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
