package com.infinities.keystone4j.identity.api.command.group;

import java.util.UUID;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateGroupCommand extends AbstractIdentityCommand implements NotifiableCommand<Group> {

	private final Group groupRef;


	public CreateGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, Group group) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.groupRef = group;
	}

	@Override
	public Group execute() throws Exception {
		String domainid = groupRef.getDomainid();
		this.getAssignmentApi().getDomain(domainid);
		IdentityDriver driver = selectIdentityDriver(domainid);
		Group group = clearDomainIdIfDomainUnaware(driver, groupRef);
		group.setId(UUID.randomUUID().toString());

		Group ref = driver.createGroup(group.getId(), group);
		return setDomainIdAndMapping(ref, domainid, driver, EntityType.GROUP);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return groupRef;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
