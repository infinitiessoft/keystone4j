package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.token.TokenApi;

public class ListRoleAssignmentsForRoleCommand extends AbstractAssignmentCommand<List<Assignment>> {

	private final String roleid;


	public ListRoleAssignmentsForRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.roleid = roleid;
	}

	@Override
	public List<Assignment> execute() {
		List<Assignment> assignments = this.getAssignmentDriver().listRoleAssignments();

		List<Assignment> ret = Lists.newArrayList();
		for (Assignment assignment : assignments) {
			if (roleid.equals(assignment.getRole().getId())) {
				ret.add(assignment);
			}
		}

		return ret;
	}
}
