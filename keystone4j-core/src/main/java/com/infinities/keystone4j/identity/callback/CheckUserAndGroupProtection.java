package com.infinities.keystone4j.identity.callback;

import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.PolicyCredentialChecker;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;

public class CheckUserAndGroupProtection extends PolicyCredentialChecker implements Callback {

	private final String groupid;
	private final String userid;
	private final IdentityApi identityApi;


	public CheckUserAndGroupProtection(String userid, String groupid, IdentityApi identityApi, TokenApi tokenApi,
			PolicyApi policyApi) {
		super(tokenApi, policyApi);
		this.userid = userid;
		this.groupid = groupid;
		this.identityApi = identityApi;

	}

	@Override
	public void execute(KeystoneContext context, Action<?> command, Map<String, Object> parMap) {
		Map<String, PolicyEntity> target = Maps.newHashMap();
		Group group = this.identityApi.getGroup(groupid, null);
		target.put("group", group);
		User user = this.identityApi.getUser(userid, null);
		target.put("user", user);
		checkProtection(context, command, target, parMap);
	}

}
