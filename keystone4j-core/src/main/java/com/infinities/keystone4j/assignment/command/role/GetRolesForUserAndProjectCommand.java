package com.infinities.keystone4j.assignment.command.role;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrantMetadata;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserDomainGrantMetadata;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrantMetadata;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.MetadataNotFoundException;
import com.infinities.keystone4j.exception.NotImplementedException;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
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
				GroupProjectGrant grant = this.getAssignmentDriver().getGroupProjectGrant(group.getId(), projectid);
				Set<GroupProjectGrantMetadata> metadatas = grant.getMetadatas();
				for (GroupProjectGrantMetadata metadata : metadatas) {
					roles.add(metadata.getRole());
				}
			} catch (MetadataNotFoundException e) {
				// no group grant, skip
			}
			boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).getBoolValue();
			if (enabled) {
				try {
					GroupDomainGrant grant = this.getAssignmentDriver().getGroupDomainGrant(group.getId(),
							project.getDomain().getId());
					Set<GroupDomainGrantMetadata> metadatas = grant.getMetadatas();
					for (GroupDomainGrantMetadata metadata : metadatas) {
						roles.add(metadata.getRole());
					}
				} catch (MetadataNotFoundException e) {
					// no group grant, skip
				} catch (NotImplementedException e) {
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
			UserProjectGrant grant = this.getAssignmentDriver().getUserProjectGrant(userid, projectid);
			Set<UserProjectGrantMetadata> metadatas = grant.getMetadatas();
			for (UserProjectGrantMetadata metadata : metadatas) {
				roles.add(metadata.getRole());
			}
		} catch (MetadataNotFoundException e) {
			// no group grant, skip
		}
		boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).getBoolValue();
		if (enabled) {
			try {
				UserDomainGrant grant = this.getAssignmentDriver().getUserDomainGrant(userid, project.getDomain().getId());
				Set<UserDomainGrantMetadata> metadatas = grant.getMetadatas();
				for (UserDomainGrantMetadata metadata : metadatas) {
					roles.add(metadata.getRole());
				}
			} catch (MetadataNotFoundException e) {
				// no user grant, skip
			} catch (NotImplementedException e) {
				// ignore
			}
		}

		return roles;
	}
}
