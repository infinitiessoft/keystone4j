package com.infinities.keystone4j.assignment.api.command.grant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;

public class DeleteGrantCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteGrantCommand.class);
	private final String roleid;
	private final String userid;
	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final boolean inheritedToProjects;


	public DeleteGrantCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
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
	public Role execute() throws Exception {
		if (Strings.isNullOrEmpty(groupid)) {
			if (this.getRevokeApi() != null) {
				this.getRevokeApi().revokeByGrant(roleid, userid, domainid, projectid);
			}
		} else {
			try {

				for (User user : this.getIdentityApi().listUsersInGroup(groupid, null)) {
					if (!userid.equals(user.getId())) {
						this.getAssignmentApi().emitInvalidateUserTokenPersistence(user.getId());
						if (this.getRevokeApi() != null) {
							this.getRevokeApi().revokeByGrant(roleid, userid, domainid, projectid);
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Group {} not found, no tokens to invalidate.", groupid);
			}
		}
		this.getAssignmentDriver().deleteGrant(roleid, userid, groupid, domainid, projectid, inheritedToProjects);

		if (!Strings.isNullOrEmpty(userid)) {
			this.getAssignmentApi().emitInvalidateUserTokenPersistence(userid);
		}
		return null;
	}

}
