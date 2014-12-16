package com.infinities.keystone4j.identity.controller.action.group;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListGroupsForUserAction extends AbstractGroupAction implements FilterProtectedAction<Group> {

	private final String userid;


	public ListGroupsForUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String userid) {
		super(identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
	}

	@Override
	public CollectionWrapper<Group> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Group> users = this.getIdentityApi().listGroupsForUser(userid, hints);
		CollectionWrapper<Group> wrapper = wrapCollection(request, users, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_groups_for_user";
	}
}
