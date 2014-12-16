package com.infinities.keystone4j.trust.controller.action;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.DecoderException;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GetTrustAction extends AbstractTrustAction implements ProtectedAction<Trust> {

	private final String trustid;


	public GetTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
	}

	@Override
	public MemberWrapper<Trust> execute(ContainerRequestContext request) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		Trust trust = getTrust(request, trustid);
		return wrapMember(request, trust);
	}

	@Override
	public String getName() {
		return "get_trust";
	}
}
