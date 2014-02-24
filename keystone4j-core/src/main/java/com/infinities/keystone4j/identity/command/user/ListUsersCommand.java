package com.infinities.keystone4j.identity.command.user;

import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class ListUsersCommand extends AbstractIdentityCommand<List<User>> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private String domainid;


	public ListUsersCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, TokenApi tokenApi,
			IdentityApi identityApi, IdentityDriver identityDriver, String domainid) {
		super(assignmentApi, credentialApi, tokenApi, identityApi, identityDriver);
		this.domainid = domainid;
	}

	@Override
	public List<User> execute() {
		if (Strings.isNullOrEmpty(domainid)) {
			domainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).getText();
		}
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		List<User> ret = driver.listUsers();

		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			for (User user : ret) {
				user.setDomain(domain);
			}
		}
		return ret;
	}

}
