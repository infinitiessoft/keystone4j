package com.infinities.keystone4j.identity.command.user;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
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


	public CheckUserInGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, TokenApi tokenApi,
			IdentityApi identityApi, IdentityDriver identityDriver, String userid, String groupid, String domainid) {
		super(assignmentApi, credentialApi, tokenApi, identityApi, identityDriver);
		this.userid = userid;
		this.groupid = groupid;
		this.domainid = domainid;
	}

	@Override
	public User execute() {
		if (Strings.isNullOrEmpty(domainid)) {
			domainid = Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).getText();
		}
		IdentityDriver driver = new IdentityUtils().selectIdentityDirver(domainid);
		driver.checkUserInGroup(userid, groupid);
		return null;
	}

}
