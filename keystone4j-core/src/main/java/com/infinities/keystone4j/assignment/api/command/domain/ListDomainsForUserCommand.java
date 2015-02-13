package com.infinities.keystone4j.assignment.api.command.domain;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;

public class ListDomainsForUserCommand extends AbstractAssignmentCommand implements TruncatedCommand<Domain> {

	private final String userid;


	public ListDomainsForUserCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
	}

	@Override
	public List<Domain> execute(Hints hints) throws Exception {
		List<String> groupids = getGroupIdsForUserId(userid);
		if (hints == null) {
			hints = new Hints();
		}
		return this.getAssignmentDriver().listDomainsForUser(userid, groupids, hints);
	}
}
