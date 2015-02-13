package com.infinities.keystone4j.identity.api.command.group;

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
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.mapping.EntityType;

public class ListGroupsCommand extends AbstractIdentityCommand implements TruncatedCommand<Group> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String domainid;


	public ListGroupsCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String domainid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.domainid = domainid;
	}

	@Override
	public List<Group> execute(Hints hints) throws Exception {
		IdentityDriver driver = selectIdentityDriver(domainid);
		if (hints == null) {
			hints = new Hints();
		}

		if (driver.isDomainAware()) {
			ensureDomainIdInHints(hints, domainid);
		} else {
			markDomainIdFilterSatisfied(hints);
		}
		List<Group> refList = driver.listGroups(hints);
		return setDomainIdAndMapping(refList, domainid, driver, EntityType.GROUP);
	}

}
