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
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserDomainGrantMetadata;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.MetadataNotFoundException;
import com.infinities.keystone4j.exception.NotImplementedException;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class GetRolesForUserAndDomainCommand extends AbstractAssignmentCommand<List<Role>> {

	private String userid;
	private String domainid;


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
				GroupDomainGrant grant = this.getAssignmentDriver().getGroupDomainGrant(group.getId(), domainid);
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
		return roles;
	}

	private List<Role> getUserDomainRoles(String userid, String domainid) {
		List<Role> roles = Lists.newArrayList();
		// _get_metadata
		try {
			UserDomainGrant grant = this.getAssignmentDriver().getUserDomainGrant(userid, domainid);
			Set<UserDomainGrantMetadata> metadatas = grant.getMetadatas();
			for (UserDomainGrantMetadata metadata : metadatas) {
				roles.add(metadata.getRole());
			}
		} catch (MetadataNotFoundException e) {
			// no group grant, skip
		} catch (NotImplementedException e) {
			// ignore
		}
		return roles;
	}
}
