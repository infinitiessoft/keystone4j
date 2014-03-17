package com.infinities.keystone4j.token;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;

public class TokenDataHelperFactory implements Factory<TokenDataHelper> {

	// private final Logger logger =
	// LoggerFactory.getLogger(TokenDataHelperFactory.class);

	private final IdentityApi identityApi;
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;


	@Inject
	public TokenDataHelperFactory(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
	}

	@Override
	public void dispose(TokenDataHelper arg0) {

	}

	@Override
	public TokenDataHelper provide() {
		return new TokenDataHelper(identityApi, assignmentApi, catalogApi);

	}
}
