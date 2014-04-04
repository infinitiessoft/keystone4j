package com.infinities.keystone4j.assignment.command.role;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.UserDomainGrant;
import com.infinities.keystone4j.model.assignment.UserProjectGrant;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.token.TokenApi;

public class GetRolesForUserAndProjectCommand extends AbstractAssignmentCommand<List<Role>> {

	private final String userid;
	private final String projectid;
	private final static String ENABLED = "enabled";


	public GetRolesForUserAndProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid, String projectid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public List<Role> execute() {
		Project project = this.getAssignmentApi().getProject(projectid);
		List<Role> userRoleList = getUserProjectRoles(userid, project);
		List<Role> groupRoleList = getGroupProjectRoles(userid, project);

		Set<Role> set = Sets.newHashSet();
		set.addAll(userRoleList);
		set.addAll(groupRoleList);

		return Lists.newArrayList(set);
	}

	private List<Role> getGroupProjectRoles(String userid, Project project) {
		List<Role> roles = Lists.newArrayList();
		List<Group> groups = this.getIdentityApi().listGroupsForUser(userid, null);
		for (Group group : groups) {
			// _get_metadata
			try {
				List<GroupProjectGrant> grants = this.getAssignmentDriver().getGroupProjectGrants(group.getId(), projectid);
				for (GroupProjectGrant grant : grants) {
					roles.add(grant.getRole());
				}
			} catch (Exception e) {
				// no group grant, skip
			}
			boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).asBoolean();
			if (enabled) {
				try {
					List<GroupDomainGrant> grants = this.getAssignmentDriver().getGroupDomainGrants(group.getId(),
							project.getDomain().getId());
					// Set<GroupDomainGrantMetadata> metadatas =
					// grant.getGroupDomainGrantMetadatas();
					for (GroupDomainGrant grant : grants) {
						roles.add(grant.getRole());
					}
				} catch (Exception e) {
					// ignore
				}
			}

		}
		return roles;
	}

	private List<Role> getUserProjectRoles(String userid, Project project) {
		List<Role> roles = Lists.newArrayList();
		// _get_metadata
		try {
			List<UserProjectGrant> grants = this.getAssignmentDriver().getUserProjectGrants(userid, projectid);
			for (UserProjectGrant grant : grants) {
				roles.add(grant.getRole());
			}
		} catch (Exception e) {
			// no group grant, skip
		}
		boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).asBoolean();
		if (enabled) {
			try {
				List<UserDomainGrant> grants = this.getAssignmentDriver().getUserDomainGrants(userid,
						project.getDomain().getId());
				for (UserDomainGrant grant : grants) {
					roles.add(grant.getRole());
				}
			} catch (Exception e) {
				// ignore
			}
		}

		return roles;
	}
}
