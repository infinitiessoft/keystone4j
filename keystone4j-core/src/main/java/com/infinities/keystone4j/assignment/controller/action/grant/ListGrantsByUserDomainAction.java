package com.infinities.keystone4j.assignment.controller.action.grant;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListGrantsByUserDomainAction extends AbstractGrantAction implements FilterProtectedAction<Role> {

	private final String userid;
	private final String domainid;


	public ListGrantsByUserDomainAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String userid, String domainid) {
		super(assignmentApi, identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
		this.domainid = domainid;
	}

	@Override
	public CollectionWrapper<Role> execute(ContainerRequestContext request, String... filters) throws Exception {
		List<Role> refs = assignmentApi.listGrantsByUserDomain(userid, domainid, checkIfInherited(request));
		CollectionWrapper<Role> wrapper = wrapCollection(request, refs);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
