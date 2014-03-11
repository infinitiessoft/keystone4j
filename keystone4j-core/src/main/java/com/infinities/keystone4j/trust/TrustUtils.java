package com.infinities.keystone4j.trust;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustRole;

public class TrustUtils {

	private TrustUtils() {

	}

	public static void trustorTrusteeOnly(Trust trust, String userid) {
		if (!userid.equals(trust.getTrustee().getId()) && !userid.equals(trust.getTrustor().getId())) {
			throw Exceptions.ForbiddenException.getInstance();
		}
	}

	public static void adminTrustorOnly(KeystoneContext context, Trust trust, String userid) {
		if (!userid.equals(trust.getTrustor().getId()) && !context.isAdmin()) {
			throw Exceptions.ForbiddenException.getInstance();
		}
	}

	public static void fillInRoles(Trust trust, List<Role> allRoles) {
		// TODO handle expireAt date format issue
		List<TrustRole> trustFullRoles = Lists.newArrayList();

		for (TrustRole trustRole : trust.getTrustRoles()) {
			List<Role> matchingRoles = Lists.newArrayList();

			for (Role role : allRoles) {
				if (role.getId().equals(trustRole.getRole().getId())) {
					matchingRoles.add(role);
				}

				if (!matchingRoles.isEmpty()) {
					trustFullRoles.add(new TrustRole(trust, matchingRoles.get(0)));
				}

			}
			trust.setTrustRoles(Sets.newHashSet(trustFullRoles));
		}
	}

}
