package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckGrantAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String userid;
	private final String roleid;
	private final String groupid;
	private final String domainid;
	private final String projectid;


	public CheckGrantAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, String roleid, String userid, String groupid, String domainid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
		this.roleid = roleid;
		this.groupid = groupid;
		this.domainid = domainid;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) throws Exception {
		requireDomainXorProject(domainid, projectid);
		requireUserXorGroup(userid, groupid);
		assignmentApi.getGrant(roleid, userid, groupid, domainid, projectid, checkIfInherited(request));
		return null;
	}

	@Override
	public String getName() {
		return "check_grant";
	}
}
