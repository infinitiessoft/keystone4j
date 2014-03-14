package com.infinities.keystone4j.trust.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.KeystoneUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.model.Trust;

public class ListTrustsAction extends AbstractTrustAction<List<Trust>> {

	private final String trustorid;
	private final String trusteeid;


	public ListTrustsAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			String trustorid, String trusteeid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustorid = trustorid;
		this.trusteeid = trusteeid;
	}

	@Override
	public List<Trust> execute(ContainerRequestContext request) {
		List<Trust> trusts = Lists.newArrayList();
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		User callingUser = new KeystoneUtils().getUser(context);

		if (Strings.isNullOrEmpty(trustorid) && Strings.isNullOrEmpty(trusteeid)) {
			new KeystoneUtils().assertAdmin(context);
			trusts = this.getTrustApi().listTrusts();
		}

		if (!Strings.isNullOrEmpty(trustorid)) {
			if (!trustorid.equals(callingUser.getId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			trusts.addAll(this.getTrustApi().listTrustsForTrustor(trustorid));
		}

		if (!Strings.isNullOrEmpty(trusteeid)) {
			if (!trusteeid.equals(callingUser.getId())) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			trusts.addAll(this.getTrustApi().listTrustsForTrustee(trusteeid));
		}

		return trusts;
	}

	@Override
	public String getName() {
		return "list_trust";
	}
}
