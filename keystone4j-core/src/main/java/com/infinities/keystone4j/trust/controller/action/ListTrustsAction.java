package com.infinities.keystone4j.trust.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class ListTrustsAction extends AbstractTrustAction<List<Trust>> {

	private final String trustorid;
	private final String trusteeid;
	private final PolicyApi policyApi;


	public ListTrustsAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi, TokenApi tokenApi,
			PolicyApi policyApi, String trustorid, String trusteeid) {
		super(assignmentApi, identityApi, trustApi, tokenApi);
		this.trustorid = trustorid;
		this.trusteeid = trusteeid;
		this.policyApi = policyApi;
	}

	@Override
	public List<Trust> execute(ContainerRequestContext request) {
		List<Trust> trusts = Lists.newArrayList();
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		User callingUser = new KeystoneUtils().getUser(tokenApi, context);

		if (Strings.isNullOrEmpty(trustorid) && Strings.isNullOrEmpty(trusteeid)) {
			new KeystoneUtils().assertAdmin(policyApi, tokenApi, context);
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
