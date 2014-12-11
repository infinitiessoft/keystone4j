package com.infinities.keystone4j.trust.controller.action;

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

public class ListRolesForTrustAction extends AbstractTrustAction<List<Role>> {

	private final String trustid;


	public ListRolesForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenApi tokenApi, String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
	}

	@Override
	public List<Role> execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(tokenApi, context);

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

	@Override
	public String getName() {
		return "list_roles_for_trust";
	}
}
