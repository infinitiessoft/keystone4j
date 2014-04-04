package com.infinities.keystone4j.assignment.command.role;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.UserDomainGrant;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.token.TokenApi;

public class GetRolesForUserAndDomainCommand extends AbstractAssignmentCommand<List<Role>> {

	private final String userid;
	private final String domainid;


	public GetRolesForUserAndDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String userid, String domainid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.userid = userid;
		this.domainid = domainid;
	}

	@Override
	public List<Role> execute() {
		// TODO testing?
		this.getAssignmentDriver().getDomain(domainid);
		List<Role> userRoleList = getUserDomainRoles(userid, domainid);
		List<Role> groupRoleList = getGroupDomainRoles(userid, domainid);

		Set<Role> set = Sets.newHashSet();
		set.addAll(userRoleList);
		set.addAll(groupRoleList);

		return Lists.newArrayList(set);
	}

	private List<Role> getGroupDomainRoles(String userid, String domainid) {
		List<Role> roles = Lists.newArrayList();
		List<Group> groups = this.getIdentityApi().listGroupsForUser(userid, null);
		for (Group group : groups) {
			// _get_metadata
			try {
				List<GroupDomainGrant> grants = this.getAssignmentDriver().getGroupDomainGrants(group.getId(), domainid);
				// Set<GroupDomainGrantMetadata> metadatas =
				// grant.getGroupDomainGrantMetadatas();
				for (GroupDomainGrant grant : grants) {
					roles.add(grant.getRole());
				}
			} catch (Exception e) {
				// no group grant, skip
			}
		}
		return roles;
	}

	private List<Role> getUserDomainRoles(String userid, String domainid) {
		List<Role> roles = Lists.newArrayList();
		// _get_metadata
		try {
			List<UserDomainGrant> grants = this.getAssignmentDriver().getUserDomainGrants(userid, domainid);
			for (UserDomainGrant grant : grants) {
				roles.add(grant.getRole());
			}
		} catch (Exception e) {
			// no group grant, skip
		}
		return roles;
	}
}
