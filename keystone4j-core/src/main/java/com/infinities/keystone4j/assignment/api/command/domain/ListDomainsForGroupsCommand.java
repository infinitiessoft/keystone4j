package com.infinities.keystone4j.assignment.api.command.domain;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;

public class ListDomainsForGroupsCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Domain>> {

	private final List<String> groupids;


	public ListDomainsForGroupsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, List<String> groupids) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.groupids = groupids;
	}

	@Override
	public List<Domain> execute() {
		return this.getAssignmentDriver().listDomainsForGroups(groupids);
	}
}
