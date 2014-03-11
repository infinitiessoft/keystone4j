package com.infinities.keystone4j.assignment.command.grant;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteGrantByGroupDomainCommand extends AbstractAssignmentCommand<Role> {

	private final Logger logger = LoggerFactory.getLogger(DeleteGrantByGroupDomainCommand.class);
	private final static String GROUP_NOT_FOUND = "Group {} not found, no tokens to invalidate.";
	private final String roleid;
	private final String groupid;
	private final String domainid;
	private final boolean inherited;


	public DeleteGrantByGroupDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
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
		List<User> users = Lists.newArrayList();

		if (!Strings.isNullOrEmpty(groupid)) {
			try {
				users = this.getIdentityApi().listUsersInGroup(groupid, domainid);
			} catch (Exception e) {
				logger.debug(GROUP_NOT_FOUND, groupid);
			}
		}

		this.getAssignmentDriver().deleteGrantByGroupDomain(roleid, groupid, domainid, inherited);
		for (User user : users) {
			this.getTokenApi().deleteTokensForUser(user.getId(), null);
		}
		return null;
	}
}
