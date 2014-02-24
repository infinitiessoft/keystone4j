package com.infinities.keystone4j.identity.command.group;

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
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class ListGroupsCommand extends AbstractIdentityCommand<List<Group>> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private String domainid;


	public ListGroupsCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, TokenApi tokenApi,
			IdentityApi identityApi, IdentityDriver identityDriver, String domainid) {
		super(assignmentApi, credentialApi, tokenApi, identityApi, identityDriver);
		this.domainid = domainid;
	}

	@Override
	public List<Group> execute() {
		if (Strings.isNullOrEmpty(domainid)) {
			domainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).getText();
		}
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		List<Group> ret = driver.listGroups();

		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			for (Group group : ret) {
				group.setDomain(domain);
			}
		}
		return ret;
	}

}
