package com.infinities.keystone4j.identity.command.user;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class CreateUserCommand extends AbstractIdentityCommand<User> {

	private final User user;


	public CreateUserCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, TokenApi tokenApi,
			IdentityApi identityApi, IdentityDriver identityDriver, User user) {
		super(assignmentApi, credentialApi, tokenApi, identityApi, identityDriver);
		this.user = user;
	}

	@Override
	public User execute() {
		Domain domain = user.getDomain();
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domain.getId());

		if (!driver.isDomainAware()) {
			new IdentityUtils().clearDomainid(user);
		}
		User ret = driver.createUser(user);
		if (!driver.isDomainAware()) {
			ret.setDomain(domain);
		}
		return ret;
	}

}
