package com.infinities.keystone4j.token.provider.api.command;

import java.util.Date;
import java.util.List;

import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class IssueV3TokenCommand extends AbstractTokenProviderCommand<TokenIdAndData> {

	private final String userid;
	private final List<String> methodNames;
	private final Date expiresAt;
	private final String projectid;
	private final String domainid;
	private final AuthContext authContext;
	private final Trust trust;
	private final Token token;
	private final boolean includeCatalog;


	public IssueV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String userid,
			List<String> methodNames, Date expiresAt, String projectid, String domainid, AuthContext authContext,
			Trust trust, Token token, boolean includeCatalog) {
		super(tokenProviderApi, tokenProviderDriver);
		this.userid = userid;
		this.methodNames = methodNames;
		this.expiresAt = expiresAt;
		this.projectid = projectid;
		this.domainid = domainid;
		this.authContext = authContext;
		this.trust = trust;
		this.token = token;
		this.includeCatalog = includeCatalog;
	}

	@Override
	public TokenIdAndData execute() {
		return this.getTokenProviderDriver().issueV3Token(userid, methodNames, expiresAt, projectid, domainid, authContext,
				trust, token, includeCatalog);
	}

}
