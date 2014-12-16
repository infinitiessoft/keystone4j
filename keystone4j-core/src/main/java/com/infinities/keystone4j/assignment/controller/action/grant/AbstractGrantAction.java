package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.role.v3.AbstractRoleAction;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractGrantAction extends AbstractRoleAction {

	protected IdentityApi identityApi;


	public AbstractGrantAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.identityApi = identityApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public boolean checkIfInherited(ContainerRequestContext request) {
		String path = request.getUriInfo().getPath();
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return (Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean() && path.startsWith("/OS-INHERIT") && path
				.endsWith("/inherited_to_projects"));
	}
}
