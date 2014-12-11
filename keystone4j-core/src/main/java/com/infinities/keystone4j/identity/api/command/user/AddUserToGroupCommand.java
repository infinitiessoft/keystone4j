package com.infinities.keystone4j.identity.api.command.user;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class AddUserToGroupCommand extends AbstractIdentityCommand<User> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final String groupid;
	private String domainid;


	public AddUserToGroupCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver, String userid, String groupid, String domainid) {
		super(credentialApi, tokenApi, identityApi, identityDriver);
		this.userid = userid;
		this.groupid = groupid;
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
		driver.addUserToGroup(userid, groupid);
		this.getTokenApi().deleteTokensForUser(userid, null);
		return null;
	}

}
