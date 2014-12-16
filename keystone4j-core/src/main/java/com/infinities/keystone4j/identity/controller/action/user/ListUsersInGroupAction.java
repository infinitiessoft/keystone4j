package com.infinities.keystone4j.identity.controller.action.user;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListUsersInGroupAction extends AbstractUserAction implements FilterProtectedAction<User> {

	private final String groupid;


	public ListUsersInGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String groupid) {
		super(identityApi, tokenProviderApi, policyApi);
		this.groupid = groupid;
	}

	@Override
	public CollectionWrapper<User> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<User> users = this.getIdentityApi().listUsersInGroup(groupid, hints);
		CollectionWrapper<User> wrapper = wrapCollection(request, users, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_users_in_group";
	}
}
