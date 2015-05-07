package com.infinities.keystone4j.policy.check;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.Enforcer;

public class RoleCheck extends Check {

	private final static Logger logger = LoggerFactory.getLogger(RoleCheck.class);


	@Override
	public String getRule() {
		return "role";
	}

	@Override
	public boolean check(Map<String, Object> target, Context creds, Enforcer enforcer) {
		Set<String> roles = Sets.newHashSet();
		logger.debug("creds roles: {}", creds.getRoles());
		for (String role : creds.getRoles()) {
			roles.add(role.toLowerCase());
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
