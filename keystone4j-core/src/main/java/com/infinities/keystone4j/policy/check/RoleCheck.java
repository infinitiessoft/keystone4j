package com.infinities.keystone4j.policy.check;

import java.util.Set;

import com.google.common.collect.Sets;
import com.infinities.keystone4j.policy.Enforcer;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.model.TrustRole;

public class RoleCheck extends Check {

	@Override
	public String getRule() {
		return "role";
	}

	@Override
	public boolean check(Target target, Token token, Enforcer enforcer) {
		// TODO are roles from trust?
		Set<String> roles = Sets.newHashSet();
		Set<TrustRole> trustRoles = token.getTrust().getTrustRoles();
		for (TrustRole trustRole : trustRoles) {
			roles.add(trustRole.getRole().getName());
		}
		return roles.contains(this.getMatch().toLowerCase());
	}

	@Override
	public Check newInstance(String kind, String match) {
		Check check = new RoleCheck();
		check.setKind(kind);
		check.setMatch(match);
		return check;
	}
}
