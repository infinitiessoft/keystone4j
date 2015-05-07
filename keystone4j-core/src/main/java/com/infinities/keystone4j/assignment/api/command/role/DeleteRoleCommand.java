/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteRoleCommand extends AbstractAssignmentCommand implements NotifiableCommand<Role> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteRoleCommand.class);
	private final static String REFERENCE_NOT_FOUND = "Group {}, referenced in assignment for {}, not found - ignoring.";
	private final String roleid;


	public DeleteRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		try {
			deleteTokenForRole(roleid);
		} catch (Exception e) {
			// ignore
		}
		this.getAssignmentDriver().deleteRole(roleid);
		// TODO invalidate getRole
		return null;
	}

	private void deleteTokenForRole(String roleid) throws Exception {
		List<Assignment> assignments = this.getAssignmentApi().listRoleAssignmentsForRole(roleid);
		Set<String> userids = Sets.newHashSet();
		List<Payload> userAndProjects = Lists.newArrayList();

		for (Assignment assignment : assignments) {
			if (!Strings.isNullOrEmpty(assignment.getUserId())) {
				if (!Strings.isNullOrEmpty(assignment.getProjectId())) {
					userAndProjects.add(new Payload(assignment.getUserId(), assignment.getProjectId()));
				} else if (!Strings.isNullOrEmpty(assignment.getDomainId())) {
					this.getAssignmentApi().emitInvalidateUserTokenPersistence(assignment.getUserId());
					// userids.add(assignment.getUser().getId());
				}
			} else if (!Strings.isNullOrEmpty(assignment.getGroupId())) {
				List<User> users = Lists.newArrayList();
				try {
					users = this.getIdentityApi().listUsersInGroup(assignment.getGroupId(), null);
				} catch (Exception e) {
					String arg1 = null;
					if (!Strings.isNullOrEmpty(assignment.getProjectId())) {
						arg1 = "Project " + assignment.getProjectId();
					} else if (!Strings.isNullOrEmpty(assignment.getDomainId())) {
						arg1 = "Domain " + assignment.getDomainId();
					} else {
						arg1 = "Unknown Target";
					}
					logger.debug(REFERENCE_NOT_FOUND, new Object[] { assignment.getGroupId(), arg1 });
					continue;
				}

				if (!Strings.isNullOrEmpty(assignment.getProjectId())) {
					for (User user : users) {
						userAndProjects.add(new Payload(user.getId(), assignment.getProjectId()));
					}
				} else if (!Strings.isNullOrEmpty(assignment.getDomainId())) {
					for (User user : users) {
						this.getAssignmentApi().emitInvalidateUserTokenPersistence(user.getId());
						// userids.add(user.getId());
					}
				}
			}
		}

		List<Payload> userAndProjectidsToAction = Lists.newArrayList();

		// if UserFromProject not in UserFromDomain then add user
		for (Payload userAndProject : userAndProjects) {
			if (!userids.contains(userAndProject.getUserid())) {
				userAndProjectidsToAction.add(userAndProject);
			}
		}

		// for (String id : userids) {
		// this.getTokenApi().deleteTokensForUser(id, null);
		// }
		for (Payload userAndProject : userAndProjectidsToAction) {
			this.getAssignmentApi().emitInvalidateUserProjectTokensNotification(userAndProject);
			// this.getTokenApi().deleteTokensForUser(userAndProject.userid,
			// userAndProject.projectid);
		}

	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return roleid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
