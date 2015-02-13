package com.infinities.keystone4j.identity.api.command.user;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;

public class AddUserToGroupCommand extends AbstractIdentityCommand implements NonTruncatedCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final String groupid;


	public AddUserToGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String userid, String groupid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public User execute() throws Exception {
		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(groupid);
		// String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver groupDriver = domainIdDriverAndEntityId.getDriver();
		String groupEntityId = domainIdDriverAndEntityId.getLocalId();

		domainIdDriverAndEntityId = getEntryInfoForUser(userid);
		// domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver userDriver = domainIdDriverAndEntityId.getDriver();
		String userEntityId = domainIdDriverAndEntityId.getLocalId();

		assertUserAndGroupInSameBackend(userEntityId, userDriver, groupEntityId, groupDriver);

		groupDriver.addUserToGroup(userEntityId, groupEntityId);
		return null;
	}

	private DomainIdDriverAndEntityId getEntryInfoForUser(String publicid) throws Exception {
		return getDomainDriverAndEntityId(publicid);
	}

}
