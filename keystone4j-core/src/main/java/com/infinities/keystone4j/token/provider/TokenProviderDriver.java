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
