package com.infinities.keystone4j.token.provider.command;

import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class IssueV2TokenCommand extends AbstractTokenProviderCommand<Entry<String, TokenV2DataWrapper>> {

	private final Token tokenRef;
	private final List<Role> rolesRef;
	private final Catalog catalogRef;


	public IssueV2TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver,
			Token authTokenData, List<Role> rolesRef, Catalog catalogRef) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenRef = authTokenData;
		this.rolesRef = rolesRef;
		this.catalogRef = catalogRef;
	}

	@Override
	public Entry<String, TokenV2DataWrapper> execute() {
		TokenV2DataWrapper tokenData = this.getTokenProviderDriver().formatToken(tokenRef, rolesRef, catalogRef);
		String tokenId = this.getTokenProviderDriver().getTokenId(tokenData);
		tokenData.getAccess().getToken().setId(tokenId);
		return Maps.immutableEntry(tokenId, tokenData);
	}
}
