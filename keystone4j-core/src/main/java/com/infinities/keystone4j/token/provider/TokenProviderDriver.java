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
package com.infinities.keystone4j.token.provider;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.provider.TokenProviderApi.AuthTokenData;

public interface TokenProviderDriver {

	// void revokeToken(String tokenid);

	String getTokenId(Object tokenData);

	// TokenV2DataWrapper validateV2Token(String uniqueid);

	// TokenV2DataWrapper formatToken(Token tokenRef, List<Role> rolesRef,
	// Catalog catalogRef);

	String getTokenVersion(Object tokenRef);

	// IToken validateV2Token(IToken tokenRef);

	// TokenIdAndDataV2 issueV2Token(Token tokenRef, List<Role> rolesRef,
	// Catalog catalogRef);

	// expiresAt=null,projectid=null,domainid=null,authContext=null,trust=null,metadataRef=null,includeCatalog=true,parentAuditId=null
	TokenIdAndData issueV3Token(String userid, List<String> methodNames, Calendar expiresAt, String projectid,
			String domainid, com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext authContext,
			Trust trust, Metadata metadataRef, boolean includeCatalog, String parentAuditId) throws Exception;

	TokenV2DataWrapper validateV2Token(Token tokenRef) throws Exception;

	// should be token
	TokenDataWrapper validateV3Token(Token tokenRef) throws Exception;

	TokenIdAndDataV2 issueV2Token(AuthTokenData tokenRef, List<Role> rolesRef,
			Map<String, Map<String, Map<String, String>>> catalogRef) throws Exception;

}
