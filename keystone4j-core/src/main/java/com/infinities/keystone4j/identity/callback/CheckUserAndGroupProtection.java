package com.infinities.keystone4j.identity.callback;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.PolicyCredentialChecker;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

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
	public void execute(ContainerRequestContext request, Action<?> command, Map<String, Object> parMap) {
		Map<String, PolicyEntity> target = Maps.newHashMap();
		Group group = this.identityApi.getGroup(groupid, null);
		target.put("group", group);
		User user = this.identityApi.getUser(userid, null);
		target.put("user", user);
		Token token = (Token) request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, token, command, target, parMap);
	}

}
