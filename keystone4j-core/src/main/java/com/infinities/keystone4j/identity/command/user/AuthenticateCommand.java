package com.infinities.keystone4j.identity.command.user;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class AuthenticateCommand extends AbstractIdentityCommand<User> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final String password;
	private String domainid;


	public AuthenticateCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, String userid, String password, String domainid) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.userid = userid;
		this.password = password;
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
		User ret = driver.authenticate(userid, password);

		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			ret.setDomain(domain);
		}
		return ret;
	}

}
