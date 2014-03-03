package com.infinities.keystone4j.trust.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.exception.RoleNotFoundException;
import com.infinities.keystone4j.exception.TrustNotFoundException;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustRole;

public class GetRoleForTrustAction extends AbstractTrustAction<Role> {

	private final String trustid;
	private final String roleid;
	private HttpServletRequest request;


	public GetRoleForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustid, String roleid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(context);

		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw new TrustNotFoundException(null, trustid);
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
			throw new RoleNotFoundException(null, roleid);
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
			throw new RoleNotFoundException(null, roleid);
		}
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "get_role_for_trust";
	}
}
