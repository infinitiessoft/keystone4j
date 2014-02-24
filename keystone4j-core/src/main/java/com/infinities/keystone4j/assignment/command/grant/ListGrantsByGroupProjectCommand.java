package com.infinities.keystone4j.assignment.command.grant;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class ListGrantsByGroupProjectCommand extends AbstractAssignmentCommand<List<Role>> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupProjectCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private String groupid;
	private String projectid;
	private boolean inherited;


	public ListGrantsByGroupProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String groupid, String projectid,
			boolean inherited) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.groupid = groupid;
		this.projectid = projectid;
		this.inherited = inherited;
	}

	@Override
	public List<Role> execute() {
		return this.getAssignmentDriver().listGrantsByGroupProject(groupid, projectid, inherited);
	}
}
