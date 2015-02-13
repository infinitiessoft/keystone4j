package com.infinities.keystone4j.identity.api.command.group;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.mapping.EntityType;

public class GetGroupByNameCommand extends AbstractIdentityCommand implements NonTruncatedCommand<Group> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupName;
	private final String domainid;


	public GetGroupByNameCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String groupName,
			String domainid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.groupName = groupName;
		this.domainid = domainid;
	}

	@Override
	public Group execute() throws Exception {
		IdentityDriver driver = selectIdentityDriver(domainid);
		Group ref = driver.getGroupByName(groupName, domainid);

		return setDomainIdAndMapping(ref, domainid, driver, EntityType.GROUP);
	}

}
