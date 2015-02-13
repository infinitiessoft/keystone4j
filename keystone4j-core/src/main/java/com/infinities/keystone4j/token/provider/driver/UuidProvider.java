package com.infinities.keystone4j.token.provider.driver;

import java.util.UUID;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.trust.TrustApi;

public class UuidProvider extends BaseProvider {

	// private final static Logger logger =
	// LoggerFactory.getLogger(UuidProvider.class);

	public UuidProvider(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi, TrustApi trustApi) {
		super(assignmentApi, catalogApi, identityApi, trustApi);
	}

	@Override
	public String getTokenId(Object tokenData) {
		return UUID.randomUUID().toString();
	}
}
