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

public class ListGroupsAction extends AbstractGroupAction implements FilterProtectedAction<Group> {

	public ListGroupsAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Group> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Group> users = this.getIdentityApi().listGroups(getDomainidForListRequest(request), hints);
		CollectionWrapper<Group> wrapper = wrapCollection(request, users, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_groups";
	}
}
