package com.infinities.keystone4j.policy.check;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenRole;

public class RoleCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(RoleCheck.class);


	@Override
	public String getRule() {
		return "role";
	}

	@Override
	public boolean check(Map<String, PolicyEntity> target, Token token, Map<String, Object> parMap, Enforcer enforcer) {
		// TODO are roles from trust?
		Set<String> roles = Sets.newHashSet();
		Set<TokenRole> tokenRoles = token.getTokenRoles();
		for (TokenRole tokenRole : tokenRoles) {
			roles.add(tokenRole.getRole().getName());
		}
		return roles.contains(this.getMatch().toLowerCase());
	}

	@Override
	public Check newInstance(String kind, String match) {
		logger.debug("new role check: {}, {}", new Object[] { kind, match });
		Check check = new RoleCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
