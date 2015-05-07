package com.infinities.keystone4j.token.provider.driver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenDataHelper;
import com.infinities.keystone4j.token.V2TokenDataHelper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi.AuthTokenData;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class BaseProvider implements TokenProviderDriver {

	private final static Logger logger = LoggerFactory.getLogger(BaseProvider.class);
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final IdentityApi identityApi;
	// private CatalogApi catalogApi;
	// private final TokenApi tokenApi;
	private final TrustApi trustApi;
	private final V2TokenDataHelper v2TokenDataHelper;
	private final TokenDataHelper v3TokenDataHelper;


	// private final String DEFAULT_DOMAIN_ID =
	// Config.Instance.getOpt(Config.Type.identity,
	// "default_domain_id").asText();

	public BaseProvider(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi, TrustApi trustApi) {
		v2TokenDataHelper = new V2TokenDataHelper();
		v3TokenDataHelper = new TokenDataHelper(identityApi, assignmentApi, catalogApi);
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
	}

	@Override
	public String getTokenVersion(Object tokenData) {
		if (tokenData instanceof Token) {
			Token data = (Token) tokenData;
			if (!Strings.isNullOrEmpty(data.getTokenVersion())) {
				if (TokenProviderApi.VERSIONS.contains(data.getTokenVersion())) {
					return data.getTokenVersion();
				}
			}
		}

		if (tokenData instanceof TokenV2DataWrapper) {
			if (((TokenV2DataWrapper) tokenData).getAccess() != null) {
				return TokenProviderApi.V2;
			}
		}

		if (tokenData instanceof TokenDataWrapper) {
			TokenDataWrapper data = (TokenDataWrapper) tokenData;
			if (data.getToken() != null && data.getToken().getMethods() != null) {
				return TokenProviderApi.V3;
			}
		}

		throw Exceptions.UnsupportedTokenVersionException.getInstance();
	}

	@Override
	public TokenIdAndDataV2 issueV2Token(AuthTokenData tokenRef, List<Role> rolesRef,
			Map<String, Map<String, Map<String, String>>> catalogRef) {
		Metadata metadataRef = tokenRef.getMetadata();
		Trust trustRef = null;
		if (Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean() && metadataRef != null
				&& !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
			trustRef = trustApi.getTrust(metadataRef.getTrustId(), false);
		}

		TokenV2DataWrapper tokenData = v2TokenDataHelper.formatToken(tokenRef, rolesRef, catalogRef, trustRef);
		String tokenId = getTokenId(tokenData);
		tokenData.getAccess().getToken().setId(tokenId);
		return new TokenIdAndDataV2(tokenId, tokenData);
	}

	@Override
	public TokenIdAndData issueV3Token(String userid, List<String> methodNames, Calendar expiresAt, String projectid,
			String domainid, com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext authContext,
			Trust trust, Metadata metadataRef, boolean includeCatalog, String parentAuditId) throws Exception {
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && trust == null && metadataRef != null && !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
			trust = trustApi.getTrust(metadataRef.getTrustId(), false);
		}

		Token tokenRef = null;
		if (authContext != null && isMappedToken(authContext)) {
			tokenRef = handleMappedTokens(authContext, projectid, domainid);
		}

		if (methodNames.contains("oauth1")) {
			throw Exceptions.ForbiddenException.getInstance("Oauth is disabled.");
		}

		TokenDataWrapper tokenData = v3TokenDataHelper.getTokenData(userid, methodNames, authContext == null ? null
				: authContext.getExtras(), domainid, projectid, expiresAt, trust,
				authContext != null ? authContext.getBind() : null, tokenRef, includeCatalog, null, parentAuditId);

		String tokenid = getTokenId(tokenData);
		return new TokenIdAndData(tokenid, tokenData);
	}

	private Token handleMappedTokens(AuthContext authContext, String projectid, String domainid) {
		// TODO ignore federation
		return null;
	}

	private boolean isMappedToken(AuthContext authContext) {
		// TODO ignore federation
		return false;
	}

	// should only be Token
	@Override
	public TokenV2DataWrapper validateV2Token(Token tokenRef) {
		try {
			assertDefaultDomain(tokenRef);
			TokenV2DataWrapper tokenData = null;

			if (tokenRef.getTokenData() instanceof TokenV2DataWrapper) {
				tokenData = (TokenV2DataWrapper) tokenRef.getTokenData();
			}
			if (tokenData == null || !TokenProviderApi.V2.equals(getTokenVersion(tokenData))) {
				Metadata metadataRef = tokenRef.getMetadata();
				List<Role> rolesRef = new ArrayList<Role>();
				for (String roleId : metadataRef.getRoles()) {
					rolesRef.add(assignmentApi.getRole(roleId));
				}

				Map<String, Map<String, Map<String, String>>> catalogRef = null;
				if (tokenRef.getTenant() != null) {
					catalogRef = catalogApi
							.getCatalog(tokenRef.getUser().getId(), tokenRef.getTenant().getId(), metadataRef);
				}
				Trust trustRef = null;
				if (Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean()
						&& !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
					trustRef = trustApi.getTrust(metadataRef.getTrustId(), false);
				}
				tokenData = v2TokenDataHelper.formatToken(tokenRef, rolesRef, catalogRef, trustRef);
			}

			return tokenData;
		} catch (Exception e) {
			logger.error("Failed to validate token", e);
			throw Exceptions.TokenNotFoundException.getInstance();
		}

	}

	// should be Token
	@Override
	public TokenDataWrapper validateV3Token(Token tokenRef) throws Exception {
		TokenDataWrapper tokenData = null;
		if (tokenRef.getTokenData() instanceof TokenDataWrapper) {
			tokenData = (TokenDataWrapper) tokenRef.getTokenData();
		}

		if (tokenData == null || !(tokenData instanceof TokenDataWrapper) || tokenData.getToken() == null) {
			String projectid = null;
			logger.debug("tokenRef: {}", tokenRef);
			Project projectRef = tokenRef.getTenant();
			if (projectRef != null) {
				projectid = projectRef.getId();
			}

			logger.debug("tokendata==null? {}", String.valueOf(tokenRef.getTokenData() == null));
			Calendar issuedAt = ((TokenV2DataWrapper) tokenRef.getTokenData()).getAccess().getToken().getIssued_at();
			List<String> audit = ((TokenV2DataWrapper) tokenRef.getTokenData()).getAccess().getToken().getAuditIds();

			List<String> methodNames = new ArrayList<String>();
			methodNames.add("password");
			methodNames.add("token");
			Map<String, String> extra = new HashMap<String, String>();

			tokenData = v3TokenDataHelper.getTokenData(tokenRef.getUser().getId(), methodNames, extra, null, projectid,
					tokenRef.getExpires(), null, null, null, true, issuedAt, audit);
		}
		logger.debug("tokenData roles: {}", tokenData.getToken().getRoles());
		return tokenData;
	}

	// should only be Token
	private void assertDefaultDomain(Token tokenRef) throws Exception {
		if (tokenRef.getTokenData() != null && TokenProviderApi.V3.equals(getTokenVersion(tokenRef.getTokenData()))) {
			String msg = "Non-default domain is not supported";

			if (!Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText()
					.equals(((TokenDataWrapper) tokenRef.getTokenData()).getToken().getUser().getDomain().getId())) {
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}

			if (((TokenDataWrapper) tokenRef.getTokenData()).getToken().getDomain() != null) {
				throw Exceptions.UnauthorizedException.getInstance("Domain scoped token is not supported");
			}

			if (((TokenDataWrapper) tokenRef.getTokenData()).getToken().getProject() != null) {
				Project project = ((TokenDataWrapper) tokenRef.getTokenData()).getToken().getProject();
				String projectDomainId = project.getDomain().getId();

				if (!Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText().equals(projectDomainId)) {
					throw Exceptions.UnauthorizedException.getInstance(msg);
				}
			}

			Metadata metadataRef = tokenRef.getMetadata();
			if (Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean()
					&& !Strings.isNullOrEmpty(metadataRef.getTrustId())) {
				Trust trustRef = trustApi.getTrust(metadataRef.getTrustId(), false);
				User trusteeUserRef = identityApi.getUser(trustRef.getTrusteeUserId());
				if (!Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText()
						.equals(trusteeUserRef.getDomainId())) {
					throw Exceptions.UnauthorizedException.getInstance(msg);
				}
				User trustorUserRef = identityApi.getUser(trustRef.getTrustorUserId());
				if (!Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText()
						.equals(trustorUserRef.getDomainId())) {
					throw Exceptions.UnauthorizedException.getInstance(msg);
				}
				Project projectRef = assignmentApi.getProject(trustRef.getProjectId());
				if (!Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText()
						.equals(projectRef.getDomainId())) {
					throw Exceptions.UnauthorizedException.getInstance(msg);
				}
			}
		}
	}

	public static Calendar getDefaultExpireTime() {
		int expireDelta = Config.Instance.getOpt(Config.Type.token, "expiration").asInteger();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, expireDelta);
		return calendar;
	}

	public static List<String> auditInfo(String parentAuditId) {
		List<String> rets = new ArrayList<String>();
		String auditId = BaseEncoding.base64Url().encode(UUID.randomUUID().toString().getBytes());
		auditId = auditId.substring(0, auditId.length() - 2);
		if (!Strings.isNullOrEmpty(parentAuditId)) {
			rets.add(auditId);
			rets.add(parentAuditId);
			return rets;
		}
		rets.add(auditId);
		return rets;
	}
}
