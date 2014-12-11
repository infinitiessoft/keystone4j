package com.infinities.keystone4j.identity.api.command.group;

import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.IdentityUtils;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteGroupCommand extends AbstractIdentityCommand<Group> {

	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupid;
	private String domainid;


	public DeleteGroupCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
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
		List<User> users = this.getIdentityApi().listUsersInGroup(groupid, domainid);
		for (User user : users) {
			this.getTokenApi().deleteTokensForUser(user.getId(), null);
		}
		driver.deleteGroup(groupid);
		return null;
	}
}
