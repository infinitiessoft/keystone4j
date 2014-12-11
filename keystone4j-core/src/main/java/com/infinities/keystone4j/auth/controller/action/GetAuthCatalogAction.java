package com.infinities.keystone4j.auth.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.CatalogWrapper;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetAuthCatalogAction extends AbstractAuthAction implements ProtectedAction<Catalog> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(GetAuthCatalogAction.class);

	public GetAuthCatalogAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TokenApi tokenApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public MemberWrapper<Catalog> execute(ContainerRequestContext context) throws Exception {
		Authorization.AuthContext authContext = AbstractAction.getAuthContext(context);
		String userid = authContext.getUserid();
		String projectid = authContext.getProjectid();

		if (Strings.isNullOrEmpty(projectid)) {
			throw Exceptions.ForbiddenException
					.getInstance("A project-scoped token is required to produce a service  catalog.");
		}

		Catalog catalog = catalogApi.getV3Catalog(userid, projectid);
		CatalogWrapper wrapper = new CatalogWrapper(catalog);
		wrapper.getLinks().setSelf(getBaseUrl(context, "auth/catalog"));
		return wrapper;
	}

	@Override
	public String getName() {
		return "get_auth_projects";
	}

}
