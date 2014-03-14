package com.infinities.keystone4j.assignment.command.grant;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class ListGrantsByGroupDomainCommand extends AbstractAssignmentCommand<List<Role>> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupDomainCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private final String groupid;
	private final String domainid;
	private final boolean inherited;


	public ListGrantsByGroupDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String groupid, String domainid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		return this.getAssignmentDriver().listGrantsByGroupDomain(groupid, domainid, inherited);
	}
}
