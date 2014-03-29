package com.infinities.keystone4j.identity.command.user;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class CheckUserInGroupCommand extends AbstractIdentityCommand<User> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final String groupid;
	private String domainid;


	public CheckUserInGroupCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
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
		driver.checkUserInGroup(userid, groupid);
		return null;
	}

}
