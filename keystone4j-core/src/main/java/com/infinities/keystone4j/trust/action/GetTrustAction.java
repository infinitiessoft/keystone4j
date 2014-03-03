package com.infinities.keystone4j.trust.action;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.TrustNotFoundException;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustUtils;
import com.infinities.keystone4j.trust.model.Trust;

public class GetTrustAction extends AbstractTrustAction<Trust> {

	private final String trustid;
	private HttpServletRequest request;


	public GetTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
	}

	@Override
	public Trust execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(context);
		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw new TrustNotFoundException(null, trustid);
		}

		TrustUtils.trustorTrusteeOnly(trust, user.getId());
		// openstack separate trust database and assignment database so they
		// need to check all roles in trust database is also in assignment
		// database. however, we use the same database so it can be ignore.
		TrustUtils.fillInRoles(trust, this.getAssignmentApi().listRoles());
		return trust;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "get_trust";
	}
}
