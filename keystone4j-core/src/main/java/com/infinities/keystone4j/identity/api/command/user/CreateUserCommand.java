package com.infinities.keystone4j.identity.api.command.user;

import java.util.UUID;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateUserCommand extends AbstractIdentityCommand implements NotifiableCommand<User> {

	private final User user;


	public CreateUserCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, User user) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.user = user;
	}

	@Override
	public User execute() throws Exception {
		String domainid = user.getDomainId();
		this.getAssignmentApi().getDomain(domainid);

		IdentityDriver driver = selectIdentityDriver(domainid);
		User user = clearDomainIdIfDomainUnaware(driver, this.user);
		user.setId(UUID.randomUUID().toString());
		User ref = driver.createUser(user.getId(), user);

		return setDomainIdAndMapping(ref, domainid, driver, EntityType.USER);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return user;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
