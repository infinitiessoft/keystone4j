package com.infinities.keystone4j.identity.command.user;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class GetUserByNameCommand extends AbstractIdentityCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userName;
	private final String domainid;


	public GetUserByNameCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, String userName, String domainid) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.userName = userName;
		this.domainid = domainid;
	}

	@Override
	public User execute() {
		// if (Strings.isNullOrEmpty(domainid)) {
		// domainid = Config.getConfig().get(Config.Type.identity,
		// DEFAULT_DOMAIN_ID);
		// }
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		if (driver == null) {
			driver = this.getIdentityDriver();
		}
		User ret = driver.getUserByName(userName, domainid);

		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			ret.setDomain(domain);
		}
		return ret;
	}

}
