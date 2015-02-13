package com.infinities.keystone4j.trust.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ListRolesForTrustAction extends AbstractTrustAction<Role> implements FilterProtectedAction<Role> {

	private final String trustid;


	public ListRolesForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
	}

	@Override
	public CollectionWrapper<Role> execute(ContainerRequestContext request, String... filters) throws Exception {
		Trust trust = getTrust(request, trustid);
		if (trust == null) {
			throw Exceptions.TrustNotFoundException.getInstance(null, trustid);
		}
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String userid = getUserId(context);
		trustorTrusteeOnly(trust, userid);

		RolesWrapper rolesWrapper = new RolesWrapper();

		rolesWrapper.setRefs(trust.getRoles());
		rolesWrapper.setLinks(trust.getRolesLinks());

		return rolesWrapper;
	}

	@Override
	public String getName() {
		return "list_roles_for_trust";
	}

	@Override
	public CollectionWrapper<Role> getCollectionWrapper() {
		return new RolesWrapper();
	}

	@Override
	public MemberWrapper<Role> getMemberWrapper() {
		return new RoleWrapper();
	}
}
