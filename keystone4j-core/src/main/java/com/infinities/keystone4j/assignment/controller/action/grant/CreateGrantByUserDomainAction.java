package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateGrantByUserDomainAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String roleid;
	private final String userid;
	private final String domainid;


	public CreateGrantByUserDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, String roleid, String userid, String domainid) {
		super(assignmentApi, identityApi, tokenProviderApi);
		this.roleid = roleid;
		this.userid = userid;
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) {
		assignmentApi.createGrantByUserDomain(roleid, userid, domainid, checkIfInherited(request));
		return null;
	}

	@Override
	public String getName() {
		return "create_grant";
	}
}
