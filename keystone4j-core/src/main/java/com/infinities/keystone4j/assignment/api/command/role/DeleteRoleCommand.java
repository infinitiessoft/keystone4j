package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteRoleCommand extends AbstractAssignmentCommand<Role> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteRoleCommand.class);
	private final static String REFERENCE_NOT_FOUND = "Group {}, referenced in assignment for {}, not found - ignoring.";
	private final String roleid;


	public DeleteRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
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

	private void deleteTokenForRole(String roleid) {

		List<Assignment> assignments = this.getAssignmentApi().listRoleAssignmentsForRole(roleid);
		Set<String> userids = Sets.newHashSet();
		List<UserAndProject> userAndProjects = Lists.newArrayList();

		for (Assignment assignment : assignments) {
			if (assignment.getUser() != null) {
				if (assignment.getProject() != null) {
					userAndProjects.add(new UserAndProject(assignment.getUser().getId(), assignment.getProject().getId()));
				} else if (assignment.getDomain() != null) {
					userids.add(assignment.getUser().getId());
				}
			} else if (assignment.getGroup() != null) {
				List<User> tmp = Lists.newArrayList();
				try {
					tmp = this.getIdentityApi().listUsersInGroup(assignment.getGroup().getId(), null);
				} catch (Exception e) {
					String arg1 = null;
					if (assignment.getProject() != null) {
						arg1 = "Project " + assignment.getProject().getId();
					} else if (assignment.getDomain() != null) {
						arg1 = "Domain " + assignment.getDomain().getId();
					} else {
						arg1 = "Unknown Target";
					}
					logger.debug(REFERENCE_NOT_FOUND, new Object[] { assignment.getGroup().getId(), arg1 });
					continue;
				}

				if (assignment.getProject() != null) {
					for (User user : tmp) {
						userAndProjects.add(new UserAndProject(user.getId(), assignment.getProject().getId()));
					}
				} else if (assignment.getDomain() != null) {
					for (User user : tmp) {
						userids.add(user.getId());
					}
				}
			}
		}

		List<UserAndProject> userAndProjectidsToAction = Lists.newArrayList();

		// if UserFromProject not in UserFromDomain then add user
		for (UserAndProject userAndProject : userAndProjects) {
			if (!userids.contains(userAndProject.userid)) {
				userAndProjectidsToAction.add(userAndProject);
			}
		}

		for (String id : userids) {
			this.getTokenApi().deleteTokensForUser(id, null);
		}
		for (UserAndProject userAndProject : userAndProjectidsToAction) {
			this.getTokenApi().deleteTokensForUser(userAndProject.userid, userAndProject.projectid);
		}

	}


	private class UserAndProject {

		private final String userid;
		private final String projectid;


		private UserAndProject(String userid, String projectid) {
			this.userid = userid;
			this.projectid = projectid;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((projectid == null) ? 0 : projectid.hashCode());
			result = prime * result + ((userid == null) ? 0 : userid.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UserAndProject other = (UserAndProject) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (projectid == null) {
				if (other.projectid != null)
					return false;
			} else if (!projectid.equals(other.projectid))
				return false;
			if (userid == null) {
				if (other.userid != null)
					return false;
			} else if (!userid.equals(other.userid))
				return false;
			return true;
		}

		private DeleteRoleCommand getOuterType() {
			return DeleteRoleCommand.this;
		}

	}
}
