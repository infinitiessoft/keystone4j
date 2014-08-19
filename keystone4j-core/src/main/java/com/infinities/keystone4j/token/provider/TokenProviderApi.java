package com.infinities.keystone4j.token.provider;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.Trust;

public interface TokenProviderApi extends Api {

	TokenMetadata issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid, String domainid,
			AuthContext authContext, Trust trust, Token token, boolean includeCatalog);

	void checkV3Token(String tokenid);

	void revokeToken(String tokenid);

	TokenDataWrapper validateV3Token(String tokenid);

	Entry<String, TokenV2DataWrapper> issueV2Token(Token authTokenData, List<Role> rolesRef, Catalog catalogRef);

	TokenV2DataWrapper validateV2Token(String tokenid, String belongsTo);

	TokenV2DataWrapper checkV2Token(String tokenid, String belongsTo);

}
