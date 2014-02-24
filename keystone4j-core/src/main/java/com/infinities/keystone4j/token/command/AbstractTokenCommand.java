package com.infinities.keystone4j.token.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class AbstractTokenCommand<T> implements Command<T> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final TokenApi tokenApi;
	private final TokenDriver tokenDriver;


	public AbstractTokenCommand(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver) {
		super();
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.tokenApi = tokenApi;
		this.tokenDriver = tokenDriver;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public TokenDriver getTokenDriver() {
		return tokenDriver;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

}
