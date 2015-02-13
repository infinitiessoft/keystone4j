package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class CreateGrantCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	private final String roleid;
	private final String userid;
	private final String groupid;
	private final String domainid;
	private final String projectid;
	// default = false
	private final boolean inheritedToProjects;


	public CreateGrantCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid, String userid, String groupid,
			String domainid, String projectid, boolean inheritedToProjects) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.roleid = roleid;
		this.userid = userid;
		this.groupid = groupid;
		this.domainid = domainid;
		this.projectid = projectid;

		this.inheritedToProjects = inheritedToProjects;
	}

	@Override
	public Role execute() {
		this.getAssignmentDriver().createGrant(roleid, userid, groupid, domainid, projectid, inheritedToProjects);
		return null;
	}

}
