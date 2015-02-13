package com.infinities.keystone4j.assignment.api.command.role;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class RemoveRoleFromUserAndProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	private final String userid;
	private final String tenantid;
	private final String roleid;


	public RemoveRoleFromUserAndProjectCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid,
			String tenantid, String roleid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
		this.tenantid = tenantid;
		this.roleid = roleid;
	}

	@Override
	public Role execute() throws Exception {
		this.getAssignmentDriver().removeRoleFromUserAndProject(userid, tenantid, roleid);
		this.getIdentityApi().emitInvalidateUserTokenPersistence(userid);
		if (this.getRevokeApi() != null) {
			this.getRevokeApi().revokeByGrant(roleid, userid, null, tenantid);
		}
		return null;
	}
}
