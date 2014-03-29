package com.infinities.keystone4j.identity.command.group;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.token.TokenApi;

public class GetGroupCommand extends AbstractIdentityCommand<Group> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupid;
	private String domainid;


	public GetGroupCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, String groupid, String domainid) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.groupid = groupid;
		this.domainid = domainid;
	}

	@Override
	public Group execute() {
		if (Strings.isNullOrEmpty(domainid)) {
			domainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText();
		}
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		if (driver == null) {
			driver = this.getIdentityDriver();
		}
		Group ret = driver.getGroup(groupid);

		if (!driver.isDomainAware()) {
			Domain domain = new Domain();
			domain.setId(domainid);
			ret.setDomain(domain);
		}
		return ret;
	}

}
