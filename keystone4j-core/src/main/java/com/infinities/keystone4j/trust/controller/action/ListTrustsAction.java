package com.infinities.keystone4j.trust.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ListTrustsAction extends AbstractTrustAction implements FilterProtectedAction<Trust> {

	public ListTrustsAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Trust> execute(ContainerRequestContext request, String... filters) throws Exception {
		List<Trust> trusts = Lists.newArrayList();
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);

		if (request.getUriInfo().getQueryParameters().isEmpty()) {
			assertAdmin(context);
			trusts.addAll(trustApi.listTrusts());
		}

		if ((request.getUriInfo().getQueryParameters().containsKey("trustor_user_id"))) {
			String userid = request.getUriInfo().getQueryParameters().getFirst("trustor_user_id");
			String callingUserId = getUserId(context);
			if (!userid.equals(callingUserId)) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			trusts.addAll(trustApi.listTrustsForTrustor(userid));
		}

		if ((request.getUriInfo().getQueryParameters().containsKey("trustee_user_id"))) {
			String userid = request.getUriInfo().getQueryParameters().getFirst("trustee_user_id");
			String callingUserId = getUserId(context);
			if (!userid.equals(callingUserId)) {
				throw Exceptions.ForbiddenException.getInstance();
			}
			trusts.addAll(trustApi.listTrustsForTrustee(userid));
		}

		return wrapCollection(request, trusts);
	}

	@Override
	public String getName() {
		return "list_trust";
	}
}
