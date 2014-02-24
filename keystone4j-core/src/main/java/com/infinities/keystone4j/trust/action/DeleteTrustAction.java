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

public class DeleteTrustAction extends AbstractTrustAction<Trust> {

	private String trustid;
	private HttpServletRequest request;


	public DeleteTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustid = trustid;
	}

	@Override
	public Trust execute() {
		Trust trust = this.getTrustApi().getTrust(trustid);
		if (trust == null) {
			throw new TrustNotFoundException(null, trustid);
		}
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		User user = new KeystoneUtils().getUser(context);
		TrustUtils.adminTrustorOnly(context, trust, user.getId());
		String userid = trust.getTrustor().getId();
		this.getTrustApi().deleteTrust(trustid);
		this.tokenApi.deleteTokensForTrust(userid, trustid);

		return null;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
