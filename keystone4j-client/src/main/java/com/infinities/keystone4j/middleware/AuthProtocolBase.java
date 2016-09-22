/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.candlepin.thumbslug.ssl.SslPemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.infinities.keystone4j.client.Config;
import com.infinities.keystone4j.middleware.exception.InvalidUserTokenException;
import com.infinities.keystone4j.middleware.exception.NetworkException;
import com.infinities.keystone4j.middleware.model.Access;
import com.infinities.keystone4j.middleware.model.AccessInfo;
import com.infinities.keystone4j.middleware.model.Bind;
import com.infinities.keystone4j.middleware.model.ServiceCatalogV2;
import com.infinities.keystone4j.middleware.model.Token;
import com.infinities.keystone4j.middleware.model.TokenWrapper;
import com.infinities.keystone4j.middleware.model.v3.ServiceCatalogV3;
import com.infinities.keystone4j.middleware.model.v3.wrapper.TokenV3Wrapper;
import com.infinities.keystone4j.middleware.model.wrapper.AccessWrapper;
import com.infinities.keystone4j.middleware.model.wrapper.RevokedWrapper;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.JsonUtils;

public abstract class AuthProtocolBase {

	private final static Logger logger = LoggerFactory.getLogger(AuthProtocolBase.class);
	public final static String CONFIG_FOLDER = "config";
	protected String signingDirname;
	protected final TokenCache tokenCache;
	protected final boolean delayAuthDecision;
	protected final boolean checkRevocationsForCached;
	protected IdentityServer identityServer;
	protected Calendar tokenRevocationListFetchedTimeProp;
	protected final String revokedFileName;
	protected final int tokenRevocationListCacheTimeout;
	protected RevokedWrapper tokenRevocationListProp;
	protected final boolean includeServiceCatalog;
	protected final String signingCertFileName;
	protected final String signingCaFileName;
	protected TokenEditor tokenEditor; // for testing


	public interface TokenEditor {

		void update(TokenWrapper data);

	}

	public enum BIND_MODE {
		disabled, permissive, strict, required, kerberos;
	}


	// private boolean delayAuthDecision;
	// private boolean includeServiceCatalog;

	public AuthProtocolBase() throws MalformedURLException {
		logger.info("Starting keystone auth_token middleware");
		delayAuthDecision = Config.Instance.getOpt(Config.Type.keystone_authtoken, "delay_auth_decision").asBoolean();
		includeServiceCatalog = Config.Instance.getOpt(Config.Type.keystone_authtoken, "include_service_catalog")
				.asBoolean();
		int httpConnectTimeout = Config.Instance.getOpt(Config.Type.keystone_authtoken, "http_connect_timeout").asInteger();
		int httpRequestMaxRetries = Config.Instance.getOpt(Config.Type.keystone_authtoken, "http_request_max_retries")
				.asInteger();

		identityServer = new IdentityServer(includeServiceCatalog, Config.Instance.getOpt(Config.Type.keystone_authtoken,
				"identity_uri").asText(), Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_uri").asText(),
				Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_host").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "auth_port").asInteger(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "auth_protocol").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "auth_admin_prefix").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "certfile").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "keyfile").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "cafile").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "insecure").asBoolean(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "admin_token").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "admin_user").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "admin_password").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "admin_tenant_name").asText(), httpConnectTimeout,
				httpRequestMaxRetries, Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_version").asText());

		signingDirname = Config.Instance.getOpt(Config.Type.keystone_authtoken, "signing_dir").asText();

		if (Strings.isNullOrEmpty(signingDirname)) {
			signingDirname = CONFIG_FOLDER;
		}

		logger.info("Using {} as cache directory for signing certificate", signingDirname);
		verifySigningDir();

		signingCertFileName = String.format("%s" + File.separator + "signing_cert.pem", signingDirname);
		signingCaFileName = String.format("%s" + File.separator + "cacert.pem", signingDirname);
		revokedFileName = String.format("%s" + File.separator + "revoked.pem", signingDirname);

		String memcacheSecurityStrategy = Config.Instance.getOpt(Config.Type.keystone_authtoken,
				"memcache_security_strategy").asText();

		tokenCache = new TokenCache(Config.Instance.getOpt(Config.Type.keystone_authtoken, "token_cache_time").asInteger(),
				Config.Instance.getOpt(Config.Type.keystone_authtoken, "hash_algorithms").asList(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "cache").asText(), Config.Instance.getOpt(
						Config.Type.keystone_authtoken, "memcached_servers").asList(), memcacheSecurityStrategy,
				Config.Instance.getOpt(Config.Type.keystone_authtoken, "memcache_secret_key").asText());

		tokenRevocationListProp = null;
		tokenRevocationListFetchedTimeProp = null;
		tokenRevocationListCacheTimeout = Config.Instance.getOpt(Config.Type.keystone_authtoken, "revocation_cache_time")
				.asInteger();
		checkRevocationsForCached = Config.Instance.getOpt(Config.Type.keystone_authtoken, "check_revocations_for_cached")
				.asBoolean();

	}

	protected Map<String, String> buildUserHeaders(TokenWrapper tokenInfo) throws IOException {
		AccessInfo authRef = AccessInfo.build(null, tokenInfo, null);
		String roles = Joiner.on(",").join(authRef.getRoleNames());

		if (tokenInfo instanceof AccessWrapper && Strings.isNullOrEmpty(authRef.getProjectId())) {
			throw new InvalidUserTokenException("Unable to determine tenancy");
		}

		Map<String, String> rval = new HashMap<String, String>();
		rval.put("X-Identity-Status", "Confirmed");
		rval.put("X-Domain-Id", authRef.getDomainId());
		rval.put("X-Domain-Name", authRef.getDomainName());
		rval.put("X-Project-Id", authRef.getProjectId());
		rval.put("X-Project-Name", authRef.getProjectName());
		rval.put("X-Project-Domain-Id", authRef.getProjectDomainId());
		rval.put("X-Project-Domain-Name", authRef.getProjectDomainName());
		rval.put("X-User-Id", authRef.getUserId());
		rval.put("X-User-Name", authRef.getUsername());
		rval.put("X-User-Domain-Id", authRef.getUserDomainId());
		rval.put("X-User-Domain-Name", authRef.getUserDomainName());
		rval.put("X-Roles", roles);
		rval.put("X-User", authRef.getUsername());
		rval.put("X-Tenant-Id", authRef.getProjectId());
		rval.put("X-Tenant-Name", authRef.getProjectName());
		rval.put("X-Tenant", authRef.getProjectName());
		rval.put("X-Role", roles);

		logger.debug("Received request from user: {} with project_id: {} and roles: {}", new Object[] { authRef.getUserId(),
				authRef.getProjectId(), roles });

		if (includeServiceCatalog && authRef.hasServiceCatalog()) {
			List<Access.Service> catalog = null;
			if (tokenInfo instanceof AccessWrapper) {
				// v2.0
				catalog = ((ServiceCatalogV2) (authRef.getServiceCatalog())).getData();
			}
			if (tokenInfo instanceof TokenV3Wrapper) {
				// v3.0
				catalog = v3ToV2Catalog(((ServiceCatalogV3) (authRef.getServiceCatalog())).getData());
			}
			rval.put("X-Service-Catalog", JsonUtils.toString(catalog));
		}

		return rval;
	}

	private List<Access.Service> v3ToV2Catalog(List<com.infinities.keystone4j.middleware.model.v3.Token.Service> data) {
		List<Access.Service> v2Services = new ArrayList<Access.Service>();
		for (com.infinities.keystone4j.middleware.model.v3.Token.Service v3Service : data) {
			Access.Service v2Service = new Access.Service();
			v2Service.setType(v3Service.getType());
			v2Service.setName(v3Service.getName());

			List<Access.Service.Endpoint> endpoints = new ArrayList<Access.Service.Endpoint>();

			for (com.infinities.keystone4j.middleware.model.v3.Token.Service.Endpoint v3Endpoint : v3Service.getEndpoints()) {
				String regionName = v3Endpoint.getRegion();
				Access.Service.Endpoint v2Endpoint = new Access.Service.Endpoint();
				v2Endpoint.setRegion(regionName);
				String interfaceName = v3Endpoint.getInterface().toLowerCase() + "URL";
				if (interfaceName.equals("adminURL")) {
					v2Endpoint.setAdminURL(v3Endpoint.getUrl());
				} else if (interfaceName.equals("publicURL")) {
					v2Endpoint.setPublicURL(v3Endpoint.getUrl());
				} else if (interfaceName.equals("internalURL")) {
					v2Endpoint.setInternalURL(v3Endpoint.getUrl());
				}
				endpoints.add(v2Endpoint);
			}
			v2Service.setEndpoints(endpoints);
			v2Services.add(v2Service);
		}

		return v2Services;
	}

	// private String getUserTokenFromHeader(ContainerRequestContext env) {
	// String token = getHeader(env, "X-Auth-Token", getHeader(env,
	// "X-Storage-Token", null));
	//
	// if (!Strings.isNullOrEmpty(token)) {
	// return token;
	// } else {
	// if (!delayAuthDecision) {
	// logger.warn("Unable to find authentication token in headers");
	// logger.debug("Headers: {}", env.getHeaders());
	// }
	// throw new InvalidUserTokenException("Unable to find token in headers");
	// }
	// }

	protected TokenWrapper validateUserToken(String userToken, String authType, String remoteUser, boolean retry) {
		String tokenId = null;

		try {
			Entry<List<String>, TokenWrapper> entry = tokenCache.get(userToken);
			List<String> tokenids = entry.getKey();
			String tokenid = entry.getKey().get(0);
			TokenWrapper cached = entry.getValue();
			TokenWrapper data = null;
			String verified = null;
			if (cached != null) {
				data = cached;
				if (checkRevocationsForCached) {
					for (String tid : tokenids) {
						boolean isRevoked = isTokenIdInRevokedList(tid);
						if (isRevoked) {
							logger.debug("Token is marked as having been revoked");
							throw new InvalidUserTokenException("Token authorization failed");
						}

					}
				}
			} else if (Cms.isPkiz(userToken)) {
				verified = verifyPkizToken(userToken, tokenids);
				data = loads(verified);
			} else if (Cms.isAsn1Token(userToken)) {
				verified = verifySignedToken(userToken, tokenids);
				data = loads(verified);
			} else {
				data = identityServer.verifyToken(userToken, retry);
			}

			// for testing
			if (tokenEditor != null) {
				tokenEditor.update(data);
			}
			Calendar expires = confirmTokenNotExpired(data);
			confirmTokenBind(data, authType, remoteUser);
			tokenCache.store(tokenid, data, expires);
			return data;
		} catch (NetworkException e) {
			logger.warn("Token validation failure.", e);
			logger.warn("Authorization failed for token");
			throw new InvalidUserTokenException("Token authorization failed");
		} catch (Exception e) {
			logger.warn("Token validation failure.", e);
			if (!Strings.isNullOrEmpty(tokenId)) {
				tokenCache.storeInvalid(tokenId);
			}
			logger.warn("Authorization failed for token");
			throw new InvalidUserTokenException("Token authorization failed");
		}
	}

	private TokenWrapper loads(String verified) throws IOException {
		try {
			TokenV3Wrapper v3Wrapper = JsonUtils.readJson(verified, TokenV3Wrapper.class);
			if (v3Wrapper.getToken() == null) {
				logger.debug("v2.0 token format: {}", verified);
				return JsonUtils.readJson(verified, AccessWrapper.class);
			} else {
				return v3Wrapper;
			}
		} catch (Exception e) {
			logger.warn("load token failed", e);
			return JsonUtils.readJson(verified, AccessWrapper.class);
		}
	}

	private String verifySignedToken(String signedText, List<String> tokenids) throws IOException, SslPemException {
		if (isSignedTokenRevoked(tokenids)) {
			throw new InvalidUserTokenException("Token has been revoked");
		}
		String formatted = Cms.tokenToCms(signedText);
		String verified = cmsVerify(formatted);
		return verified;
	}

	private String verifyPkizToken(String signedText, List<String> tokenids) throws IOException, SslPemException {
		if (isSignedTokenRevoked(tokenids)) {
			throw new InvalidUserTokenException("Token has been revoked");
		}
		try {
			String uncompressed = Cms.pkizUncompress(signedText);
			String verified = cmsVerify(uncompressed, Cms.PKIZ_CMS_FORM);
			return verified;
		} catch (Exception e) {
			throw new InvalidUserTokenException(signedText);
		}
	}

	private boolean isSignedTokenRevoked(List<String> tokenids) throws IOException, SslPemException {
		logger.debug("tokenids size: {}", tokenids.size());
		for (String tokenid : tokenids) {
			logger.debug("tokenid: {}", tokenid);
			if (isTokenIdInRevokedList(tokenid)) {
				logger.debug("Token is marked as having been revoked");
				return true;
			}
		}
		return false;
	}

	private void confirmTokenBind(TokenWrapper data, String authType, String remoteUser) {
		String bindModeStr = Config.Instance.getOpt(Config.Type.keystone_authtoken, "enforce_token_bind").asText();

		BIND_MODE bindMode = BIND_MODE.valueOf(bindModeStr);

		if (bindMode == BIND_MODE.disabled) {
			return;
		}

		Bind bind;

		try {
			bind = data.getBind();
		} catch (Exception e) {
			bind = new Bind();
		}

		boolean permissive = bindMode == BIND_MODE.permissive || bindMode == BIND_MODE.strict;

		if (bind == null || bind.isEmpty()) {
			if (permissive) {
				return;
			} else {
				logger.info("No bind information present in token.");
				throw new InvalidUserTokenException("Token authorization failed");
			}
		}

		BIND_MODE name = null;

		if (permissive || bindMode == BIND_MODE.required) {
			name = null;
		} else {
			name = bindMode;
		}

		if (name != null) {
			if (name.equals(BIND_MODE.kerberos) && bind.getKerberos() == null) {
				logger.info("Named bind mode {} not in bind information", name);
				throw new InvalidUserTokenException("Token authorization failed");
			}
		}

		if (bind.getKerberos() != null) {
			if (!"negotiate".equals(authType.toLowerCase())) {
				logger.info("Kerberos credentials required and not present.");
				throw new InvalidUserTokenException("Token authorization failed");
			}
			if (!remoteUser.equals(bind.getKerberos().getPrincipal())) {
				logger.info("Kerberos credentials do not match those in bind.");
				throw new InvalidUserTokenException("Token authorization failed");
			}
			logger.debug("Kerberos bind authentication successful.");
		} else if (BIND_MODE.permissive.equals(bindMode)) {
			logger.debug("Ignoring Unknown bind for permissive mode");
		} else {
			logger.info("Couldn't verify unknown bind");
			throw new InvalidUserTokenException("Token authorization failed");
		}
	}

	private Calendar confirmTokenNotExpired(TokenWrapper data) {
		if (data == null) {
			throw new InvalidUserTokenException("Token authorization failed");
		}
		Calendar timestamp = null;
		try {
			timestamp = data.getExpire();
		} catch (Exception e) {
			throw new InvalidUserTokenException("Token authorization failed");
		}

		if (timestamp == null) {
			throw new InvalidUserTokenException("Token authorization failed");
		}

		Calendar utcnow = Calendar.getInstance();
		logger.debug("timestamp: {}, now: {}", new Object[] { timestamp.getTime().toString(), utcnow.getTime().toString() });
		if (utcnow.after(timestamp)) {
			throw new InvalidUserTokenException("Token authorization failed");
		}

		return timestamp;
	}

	private boolean isTokenIdInRevokedList(String tid) throws IOException, SslPemException {
		RevokedWrapper revocationList = getTokenRevocationList();
		Set<Token> revokedTokens = revocationList.getRevoked();
		if (revokedTokens == null || revokedTokens.isEmpty()) {
			return false;
		}

		boolean isContained = false;
		// Set<String> revokedIds = new HashSet<String>();
		for (Token token : revokedTokens) {
			if (tid.equals(token.getId())) {
				isContained = true;
			}
		}

		return isContained;
	}

	private RevokedWrapper getTokenRevocationList() throws IOException, SslPemException {
		Calendar timeout = getTokenRevocationListFetchedTime();
		timeout.add(Calendar.SECOND, tokenRevocationListCacheTimeout);
		// long timeout = getTokenRevocationListFetchedTime() +
		// tokenRevocationListCacheTimeout;
		logger.debug("timeout: {}, now: {}, revocationTimeout: {}", new Object[] { timeout.getTime(),
				Calendar.getInstance().getTime(), tokenRevocationListCacheTimeout });
		boolean listIsCurrent = timeout.after(Calendar.getInstance());
		if (listIsCurrent) {
			if (tokenRevocationListProp == null) {
				tokenRevocationListProp = JsonUtils.readJson(new File(revokedFileName), RevokedWrapper.class);
			}
		} else {
			setTokenRevocationList(fetchRevocationList());
		}

		return tokenRevocationListProp;
	}

	private void setTokenRevocationList(String value) throws IOException {
		logger.debug("revocation cms: {}", value);
		tokenRevocationListProp = JsonUtils.readJson(value, RevokedWrapper.class);
		setTokenRevocationListFetchedTime(Calendar.getInstance());
		atomicWriteToSigningDir(revokedFileName, value);
	}

	private void atomicWriteToSigningDir(String fileName, String value) {
		try {
			Files.write(value, new File(fileName), Charsets.UTF_8);
		} catch (IOException e) {
			logger.error("cannot write revoked token to file");
		}

	}

	private String fetchRevocationList() throws SslPemException {
		String revocationListData = identityServer.fetchRevocationList(true);
		return cmsVerify(revocationListData);
	}

	private String cmsVerify(String data) {
		return cmsVerify(data, Cms.PKI_ASN1_FORM);
	}

	private String cmsVerify(String data, String inform) {
		try {
			return verify(data, inform);
		} catch (Exception e) {
			try {
				fetchSigningCert();
				fetchCaCert();
			} catch (Exception ex) {
				logger.error("save cert or ca file failed", ex);
			}
			try {
				return verify(data, inform);
			} catch (Exception ex) {
				logger.error("CMS Verify failed", ex);
				throw new RuntimeException(ex);
			}
		}

	}

	private String verify(String data, String inform) {
		try {
			return Cms.cmsVerify(data, this.signingCertFileName, this.signingCaFileName, inform);
		} catch (Exception e) {
			logger.warn("Verify error", e);
			throw new RuntimeException(e);
		}
	}

	private void fetchCaCert() throws SslPemException {
		atomicWriteToSigningDir(this.signingCaFileName, identityServer.fetchCaCert());
	}

	private void fetchSigningCert() throws SslPemException {
		atomicWriteToSigningDir(this.signingCertFileName, identityServer.fetchSigningCert());
	}

	private void setTokenRevocationListFetchedTime(Calendar value) {
		tokenRevocationListFetchedTimeProp = value;
	}

	private Calendar getTokenRevocationListFetchedTime() {
		if (tokenRevocationListFetchedTimeProp == null) {
			long fetchedTime = 0L;
			File f = new File(revokedFileName);
			if (f.exists()) {
				long mtime = f.lastModified();
				fetchedTime = mtime;
			} else {
				fetchedTime = 0L;
			}
			synchronized (this) {
				tokenRevocationListFetchedTimeProp = Calendar.getInstance();
				tokenRevocationListFetchedTimeProp.setTimeInMillis(fetchedTime);
			}
		}
		return tokenRevocationListFetchedTimeProp;
	}

	private void verifySigningDir() {
		File dir = new File(signingDirname);
		// if the directory does not exist, create it
		if (dir.exists() && dir.isDirectory()) {
			if (!dir.canWrite()) {
				throw new IllegalStateException("unable to access signing dir: " + signingDirname);
			}
		} else {
			dir.mkdir();
			dir.setWritable(true);
		}
	}

}
