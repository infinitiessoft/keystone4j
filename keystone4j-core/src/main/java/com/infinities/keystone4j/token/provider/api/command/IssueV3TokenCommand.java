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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.auth.controller.action.AbstractAuthAction;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class IssueV3TokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<TokenIdAndData> {

	private final String userid;
	private final List<String> methodNames;
	private final Calendar expiresAt;
	private final String projectid;
	private final String domainid;
	private final AbstractAuthAction.AuthContext authContext;
	private final Trust trust;
	private Metadata metadataRef;
	private final boolean includeCatalog;
	private final String parentAuditId;


	public IssueV3TokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, String userid,
			List<String> methodNames, Calendar expiresAt, String projectid, String domainid,
			AbstractAuthAction.AuthContext authContext, Trust trust, Metadata metadataRef, boolean includeCatalog,
			String parentAuditId) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.userid = userid;
		this.methodNames = methodNames;
		this.expiresAt = expiresAt;
		this.projectid = projectid;
		this.domainid = domainid;
		this.authContext = authContext;
		this.trust = trust;
		this.metadataRef = metadataRef;
		this.includeCatalog = includeCatalog;
		this.parentAuditId = parentAuditId;
	}

	@Override
	public TokenIdAndData execute() throws Exception {
		TokenIdAndData tokenIdAndData = this.getTokenProviderDriver().issueV3Token(userid, methodNames, expiresAt,
				projectid, domainid, authContext, trust, metadataRef, includeCatalog, parentAuditId);
		String tokenId = tokenIdAndData.getTokenid();
		TokenDataWrapper tokenData = tokenIdAndData.getTokenData();
		if (metadataRef == null) {
			metadataRef = new Metadata();
		}

		if (tokenData.getToken().getProject() != null) {
			Set<String> roleIds = new HashSet<String>();
			for (Role r : tokenData.getToken().getRoles()) {
				roleIds.add(r.getId());
			}
			metadataRef.setRoles(new ArrayList<String>(roleIds));
		}

		if (trust != null) {
			metadataRef.setTrustId(trust.getId());
			metadataRef.setTrusteeUserId(trust.getTrusteeUserId());
		}

		Data data = new Data();
		data.setKey(tokenId);
		data.setId(tokenId);
		data.setExpires(tokenData.getToken().getExpireAt());
		data.setUser(tokenData.getToken().getUser());
		data.setTenant(tokenData.getToken().getProject());
		data.setMetadata(metadataRef);
		data.setTokenData(tokenData);
		data.setTrustid(trust == null ? null : trust.getId());
		data.setTokenVersion(V3);
		createToken(tokenIdAndData.getTokenid(), data);
		return tokenIdAndData;
	}
}
