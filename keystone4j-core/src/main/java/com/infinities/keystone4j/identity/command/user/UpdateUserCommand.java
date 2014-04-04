package com.infinities.keystone4j.identity.command.user;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateUserCommand extends AbstractIdentityCommand<User> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final User user;
	private String domainid;


	public UpdateUserCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, String userid, User user, String domainid) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.user = user;
		this.userid = userid;
		this.domainid = domainid;
	}

	@Override
	public User execute() {
		if (Strings.isNullOrEmpty(domainid)) {
			domainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText();
		}
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		if (driver == null) {
			driver = this.getIdentityDriver();
		}

		if (!driver.isDomainAware()) {
			new IdentityUtils().clearDomainid(user);
		}
		User ret = driver.updateUser(userid, user);
		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			ret.setDomain(domain);
		}
		return ret;
	}

}
