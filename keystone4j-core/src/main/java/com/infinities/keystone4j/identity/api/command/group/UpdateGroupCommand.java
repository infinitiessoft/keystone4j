package com.infinities.keystone4j.identity.api.command.group;

import com.google.common.base.Strings;
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

public class UpdateGroupCommand extends AbstractIdentityCommand implements NotifiableCommand<Group> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupid;
	private final Group groupRef;


	public UpdateGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String groupid, Group group) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.groupRef = group;
		this.groupid = groupid;
	}

	@Override
	public Group execute() throws Exception {
		if (Strings.isNullOrEmpty(groupRef.getDomainid())) {
			this.getAssignmentApi().getDomain(groupRef.getDomainid());
		}

		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(groupid);
		String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver driver = domainIdDriverAndEntityId.getDriver();
		String entityId = domainIdDriverAndEntityId.getLocalId();
		Group group = clearDomainIdIfDomainUnaware(driver, groupRef);
		Group ref = driver.updateGroup(entityId, group);
		return setDomainIdAndMapping(ref, domainId, driver, EntityType.GROUP);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return groupid;
		} else if (index == 2) {
			return groupRef;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
