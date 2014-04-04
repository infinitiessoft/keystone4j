package com.infinities.keystone4j.assignment.command.grant;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class GetGrantByGroupDomainCommand extends AbstractAssignmentCommand<Role> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupDomainCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private final String roleid;
	private final String groupid;
	private final String domainid;
	private final boolean inherited;


	public GetGrantByGroupDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid, String groupid, String domainid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().getGrantByGroupDomain(roleid, groupid, domainid, inherited);
	}
}
