package com.infinities.keystone4j.identity.controller.callback;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckUserAndGroupProtection extends ControllerAction implements Callback {

	private final String groupid;
	private final String userid;
	private final IdentityApi identityApi;


	public CheckUserAndGroupProtection(String userid, String groupid, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.userid = userid;
		this.groupid = groupid;
		this.identityApi = identityApi;

	}

	@Override
	public void execute(ContainerRequestContext request, ProtectedAction<?> command) {
		Map<String, PolicyEntity> ref = Maps.newHashMap();
		User user = this.identityApi.getUser(userid);
		ref.put("user", user);
		Group group = this.identityApi.getGroup(groupid, null);
		ref.put("group", group);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, request, command, ref);
	}

}
