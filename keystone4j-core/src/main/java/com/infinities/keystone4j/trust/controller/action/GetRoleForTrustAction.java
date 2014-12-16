package com.infinities.keystone4j.trust.controller.action;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.codec.DecoderException;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.role.v3.AbstractRoleAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GetRoleForTrustAction extends AbstractTrustAction implements ProtectedAction<Role> {

	private final String trustid;
	private final String roleid;


	public GetRoleForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid, String roleid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) throws UnsupportedEncodingException,
			NoSuchAlgorithmException, DecoderException {
		checkRoleForTrust(request, trustid, roleid);
		Role role = assignmentApi.getRole(roleid);
		return new AbstractRoleAction(assignmentApi, tokenProviderApi, policyApi) {

		}.wrapMember(request, role);
	}

	@Override
	public String getName() {
		return "get_role_for_trust";
	}
}
