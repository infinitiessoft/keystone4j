package com.infinities.keystone4j.assignment.controller.action.role.v3;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListRolesAction extends AbstractRoleAction implements FilterProtectedAction<Role> {

	public ListRolesAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(assignmentApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Role> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Role> refs = this.getAssignmentApi().listRoles(hints);
		CollectionWrapper<Role> wrapper = wrapCollection(request, refs, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_roles";
	}
}
