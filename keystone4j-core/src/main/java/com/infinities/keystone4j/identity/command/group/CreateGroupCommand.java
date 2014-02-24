package com.infinities.keystone4j.identity.command.group;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class CreateGroupCommand extends AbstractIdentityCommand<Group> {

	private final Group group;


	public CreateGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, TokenApi tokenApi,
			IdentityApi identityApi, IdentityDriver identityDriver, Group group) {
		super(assignmentApi, credentialApi, tokenApi, identityApi, identityDriver);
		this.group = group;
	}

	@Override
	public Group execute() {
		Domain domain = group.getDomain();
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domain.getId());

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
