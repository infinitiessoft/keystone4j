package com.infinities.keystone4j.trust.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class GetTrustAction extends AbstractTrustAction<Trust> {

	private final String trustid;


	public GetTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
	}

	@Override
	public Trust execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(tokenApi, context);
		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}

		TrustUtils.trustorTrusteeOnly(trust, user.getId());
		// openstack separate trust database and assignment database so they
		// need to check all roles in trust database is also in assignment
		// database. however, we use the same database so it can be ignore.
		TrustUtils.fillInRoles(trust, this.getAssignmentApi().listRoles());
		return trust;
	}

	@Override
	public String getName() {
		return "get_trust";
	}
}
