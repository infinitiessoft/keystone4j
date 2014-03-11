package com.infinities.keystone4j.trust.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.trust.model.Trust;
import com.infinities.keystone4j.trust.model.TrustRole;

public class ListRolesForTrustAction extends AbstractTrustAction<List<Role>> {

	private final String trustid;
	private HttpServletRequest request;


	public ListRolesForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenApi tokenApi, String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
	}

	@Override
	public List<Role> execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(context);

		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}

		TrustUtils.trustorTrusteeOnly(trust, user.getId());

		List<Role> roles = Lists.newArrayList();
		for (TrustRole trustRole : trust.getTrustRoles()) {
			roles.add(trustRole.getRole());
		}

		return roles;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "list_roles_for_trust";
	}
}
