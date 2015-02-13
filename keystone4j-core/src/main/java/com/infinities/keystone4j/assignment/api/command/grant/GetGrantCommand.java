package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class GetGrantCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupDomainCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private final String roleid;
	private final String userid;
	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final boolean inherited;


	public GetGrantCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid, String userid, String groupid,
			String domainid, String projectid, boolean inherited) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
		this.roleid = roleid;
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().getGrant(roleid, userid, groupid, domainid, projectid, inherited);
	}
}
