package com.infinities.keystone4j.trust.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.wrapper.TrustWrapper;
import com.infinities.keystone4j.model.trust.wrapper.TrustsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class CheckRoleForTrustAction extends AbstractTrustAction<Trust> implements ProtectedAction<Trust> {

	private final String trustid;
	private final String roleid;


	public CheckRoleForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid, String roleid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Trust> execute(ContainerRequestContext request) throws Exception {
		checkRoleForTrust(request, trustid, roleid);
		return null;
	}

	@Override
	public String getName() {
		return "check_role_for_trust";
	}

	@Override
	public CollectionWrapper<Trust> getCollectionWrapper() {
		return new TrustsWrapper();
	}

	@Override
	public MemberWrapper<Trust> getMemberWrapper() {
		return new TrustWrapper();
	}
}
