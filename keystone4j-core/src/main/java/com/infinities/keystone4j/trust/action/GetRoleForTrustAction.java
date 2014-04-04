package com.infinities.keystone4j.trust.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class GetRoleForTrustAction extends AbstractTrustAction<Role> {

	private final String trustid;
	private final String roleid;


	public GetRoleForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustid, String roleid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
		this.roleid = roleid;
	}

	@Override
	public Role execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(tokenApi, context);

		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}

		TrustUtils.trustorTrusteeOnly(trust, user.getId());

		List<Role> matchingRoles = Lists.newArrayList();
		for (TrustRole trustRole : trust.getTrustRoles()) {
			if (roleid.equals(trustRole.getRole().getId())) {
				matchingRoles.add(trustRole.getRole());
				break;
			}
		}

		if (matchingRoles.isEmpty()) {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}

		List<Role> allRoles = this.getAssignmentApi().listRoles();
		matchingRoles = Lists.newArrayList();
		for (Role role : allRoles) {
			if (roleid.equals(role.getId())) {
				matchingRoles.add(role);
				break;
			}
		}

		if (!matchingRoles.isEmpty()) {
			return matchingRoles.get(0);
		} else {
			throw Exceptions.RoleNotFoundException.getInstance(null, roleid);
		}
	}

	@Override
	public String getName() {
		return "get_role_for_trust";
	}
}
