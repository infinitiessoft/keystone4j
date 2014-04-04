package com.infinities.keystone4j.identity.command.group;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.token.TokenApi;

public class CreateGroupCommand extends AbstractIdentityCommand<Group> {

	private final Group group;


	public CreateGroupCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, Group group) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.group = group;
	}

	@Override
	public Group execute() {
		Domain domain = group.getDomain();
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domain.getId());
		if (driver == null) {
			driver = this.getIdentityDriver();
		}

		if (!driver.isDomainAware()) {
			new IdentityUtils().clearDomainid(group);
		}
		Group ret = driver.createGroup(group);
		if (!driver.isDomainAware()) {
			ret.setDomain(domain);
		}
		return ret;
	}

}
