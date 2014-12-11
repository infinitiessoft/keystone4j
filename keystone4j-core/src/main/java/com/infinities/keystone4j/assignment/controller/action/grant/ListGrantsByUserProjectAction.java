package com.infinities.keystone4j.assignment.controller.action.grant;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListGrantsByUserProjectAction extends AbstractGrantAction implements FilterProtectedAction<Role> {

	private final String userid;
	private final String projectid;


	public ListGrantsByUserProjectAction(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, String userid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public CollectionWrapper<Role> execute(ContainerRequestContext request, String... filters) throws Exception {
		List<Role> refs = assignmentApi.listGrantsByUserProject(userid, projectid, checkIfInherited(request));
		CollectionWrapper<Role> wrapper = wrapCollection(request, refs);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_grants";
	}
}
