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

public class ListUsersCommand extends AbstractIdentityCommand implements TruncatedCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String domainid;


	public ListUsersCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String domainid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.domainid = domainid;
	}

	@Override
	public List<User> execute(Hints hints) throws Exception {
		IdentityDriver driver = selectIdentityDriver(domainid);
		if (hints == null) {
			hints = new Hints();
		}
		if (driver.isDomainAware()) {
			ensureDomainIdInHints(hints, domainid);
		} else {
			markDomainIdFilterSatisfied(hints);
		}
		List<User> refList = driver.listUsers(hints);

		return setDomainIdAndMapping(refList, domainid, driver, EntityType.USER);
	}
}
