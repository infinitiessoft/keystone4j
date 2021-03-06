/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.token.provider.api.command;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi.AuthTokenData;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class IssueV2TokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<TokenIdAndDataV2> {

	private final AuthTokenData tokenRef;
	private final List<Role> rolesRef;
	private final Map<String, Map<String, Map<String, String>>> catalogRef;


	public IssueV2TokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, AuthTokenData authTokenData,
			List<Role> rolesRef, Map<String, Map<String, Map<String, String>>> catalogRef) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.tokenRef = authTokenData;
		this.rolesRef = rolesRef;
		this.catalogRef = catalogRef;
	}

	@Override
	public TokenIdAndDataV2 execute() throws Exception {
		TokenIdAndDataV2 tokenIdAndData = this.getTokenProviderDriver().issueV2Token(tokenRef, rolesRef, catalogRef);
		Data data = new Data();
		data.setKey(tokenIdAndData.getTokenid());
		data.setId(tokenIdAndData.getTokenid());
		data.setExpires(tokenIdAndData.getTokenData().getAccess().getToken().getExpires());
		data.setUser(tokenRef.getUser());
		data.setTenant(tokenRef.getTenant());
		data.setMetadata(tokenRef.getMetadata());
		data.setTokenData(tokenIdAndData.getTokenData());
		data.setBind(tokenRef.getBind());
		data.setTrustid(tokenRef.getMetadata().getTrustId());
		data.setTokenVersion(V2);
		createToken(tokenIdAndData.getTokenid(), data);

		return tokenIdAndData;
	}

}
