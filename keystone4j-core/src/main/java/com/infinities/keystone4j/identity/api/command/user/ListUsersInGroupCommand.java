package com.infinities.keystone4j.identity.api.command.user;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.mapping.EntityType;

public class ListUsersInGroupCommand extends AbstractIdentityCommand implements TruncatedCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupid;


	public ListUsersInGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String groupid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.groupid = groupid;
	}

	@Override
	public List<User> execute(Hints hints) throws Exception {
		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(groupid);
		String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver driver = domainIdDriverAndEntityId.getDriver();
		String entityId = domainIdDriverAndEntityId.getLocalId();

		if (hints == null) {
			hints = new Hints();
		}
		if (!driver.isDomainAware()) {
			markDomainIdFilterSatisfied(hints);
		}
		List<User> refList = driver.listUsersInGroup(entityId, hints);
		return setDomainIdAndMapping(refList, domainId, driver, EntityType.USER);
	}

}
