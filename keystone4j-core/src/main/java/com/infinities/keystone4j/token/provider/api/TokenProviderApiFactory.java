package com.infinities.keystone4j.token.provider.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenProviderApiFactory implements Factory<TokenProviderApi> {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final PersistenceManager persistenceManager;
	private final RevokeApi revokeApi;
	private final TokenProviderDriver tokenProviderDriver;


	@Inject
	public TokenProviderApiFactory(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TrustApi trustApi, RevokeApi revokeApi, TokenProviderDriver tokenProviderDriver,
			PersistenceManager persistenceManager) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.revokeApi = revokeApi;
		this.tokenProviderDriver = tokenProviderDriver;
		this.persistenceManager = persistenceManager;
	}

	@Override
	public void dispose(TokenProviderApi arg0) {

	}

	@Override
	public TokenProviderApi provide() {
		TokenProviderApi tokenProviderApi;
		try {
			tokenProviderApi = new TokenProviderApiImpl(assignmentApi, catalogApi, identityApi, trustApi, revokeApi,
					tokenProviderDriver, persistenceManager);
			return tokenProviderApi;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
