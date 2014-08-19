package com.infinities.keystone4j.token.action;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.TokenBindValidator;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class AbstractTokenAction<T> extends TokenBindValidator implements Action<T> {

	protected AssignmentApi assignmentApi;
	protected CatalogApi catalogApi;
	protected IdentityApi identityApi;
	protected TokenApi tokenApi;
	protected TokenProviderApi tokenProviderApi;
	protected TrustApi trustApi;


	public AbstractTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public void setTokenApi(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public void setTokenProviderApi(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public void setTrustApi(TrustApi trustApi) {
		this.trustApi = trustApi;
	}

}
