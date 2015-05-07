//package com.infinities.keyston4j.client;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.crypto.BadPaddingException;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.NoSuchPaddingException;
//import javax.ws.rs.container.ContainerRequestContext;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.base.Strings;
//import com.infinities.keystone4j.exception.InvalidUserTokenException;
//import com.infinities.keystone4j.exception.ServiceException;
//import com.infinities.keystone4j.utils.Cms;
//
//public class AuthProtocol {
//
//	private final static Logger logger = LoggerFactory.getLogger(AuthProtocol.class);
//	private final static String CACHE_KEY_TEMPLATE = "tokens/";
//	private String memcacheSecurityStrategy;
//	private String memcacheSecretKey;
//	private boolean cacheInitialized;
//	private boolean delayAuthDecision;
//	private int tokenRevocationListFetchedTime;
//	private int tokenRevocationListCacheTimeout;
//	private String revokedFileName;
//	private List<String> tokenRevocationList;
//	private Cms cms;
//	private final static List<String> AUTH_HEADERS = new ArrayList<String>();
//
//	static {
//		AUTH_HEADERS.add("X-Identity-Status");
//		AUTH_HEADERS.add("X-Domain-Id");
//		AUTH_HEADERS.add("X-Domain-Name");
//		AUTH_HEADERS.add("X-Project-Id");
//		AUTH_HEADERS.add("X-Project-Name");
//		AUTH_HEADERS.add("X-Project-Domain-Id");
//		AUTH_HEADERS.add("X-Project-Domain-Name");
//		AUTH_HEADERS.add("X-User-Id");
//		AUTH_HEADERS.add("X-User-Name");
//		AUTH_HEADERS.add("X-User-Domain-Id");
//		AUTH_HEADERS.add("X-User-Domain-Name");
//		AUTH_HEADERS.add("X-Roles");
//		AUTH_HEADERS.add("X-Service-Catalog");
//		AUTH_HEADERS.add("X-User");
//		AUTH_HEADERS.add("X-Tenant-Id");
//		AUTH_HEADERS.add("X-Tenant-Name");
//		AUTH_HEADERS.add("X-Tenant");
//		AUTH_HEADERS.add("X-Role");
//
//	}
//
//
//	public enum BindMode {
//		disabled, permissive, strict, required, kerberos
//	}
//
//	public enum MemcaheSecurityStrategy {
//		MAC, ENCRYPT
//	}
//
//
//	public AuthProtocol() {
//		setupCms();
//		init();
//	}
//
//	private void setupCms() {
//		String keyFile = Config.Instance.getOpt(Config.Type.keystone_authtoken, "").asText();
//
//		cms = new Cms();
//	}
//
//	private void init() {
//		logger.info("Starting keystone auth token middleware");
//
//		delayAuthDecision = Config.Instance.getOpt(Config.Type.keystone_authtoken, "delay_auth_decision").asBoolean();
//		String identityUri = Config.Instance.getOpt(Config.Type.keystone_authtoken, "identity_uri").asText();
//		String authUri = Config.Instance.getOpt(Config.Type.keystone_authtoken, "auth_uri").asText();
//
//		if (Strings.isNullOrEmpty(identityUri)) {
//			throw new IllegalStateException(
//					"Configuring admin URI using auth fragments. This is deprecated, use 'identity_uri' instead.");
//
//		} else {
//			identityUri = identityUri.endsWith("/") ? identityUri.substring(0, identityUri.length() - 1) : identityUri;
//		}
//
//		// SSL
//		String certFile = Config.Instance.getOpt(Config.Type.keystone_authtoken, "certfile").asText();
//		String keyFile = Config.Instance.getOpt(Config.Type.keystone_authtoken, "keyfile").asText();
//		String sslCaFile = Config.Instance.getOpt(Config.Type.keystone_authtoken, "cafile").asText();
//		String sslInsecure = Config.Instance.getOpt(Config.Type.keystone_authtoken, "insecure").asText();
//
//		// signing
//		String signingDir = Config.Instance.getOpt(Config.Type.keystone_authtoken, "signing_dir").asText();
//		if (Strings.isNullOrEmpty(signingDir)) {
//			signingDir = "keystone-signing/";
//		}
//		logger.info("Using {} as cache directory for signing certificate", signingDir);
//		verifySigningDir(signingDir);
//
//		String signingCertFileName = signingDir + "/signing_cert.pem";
//		String signingCaFileName = signingDir + "/cacert.pem";
//		revokedFileName = signingDir + "/revoked.pem";
//
//		String adminToken = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_token").asText();
//		String adminTokenExpiry = null;
//		String adminUser = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_user").asText();
//		String adminPassword = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_password").asText();
//		String adminTenantName = Config.Instance.getOpt(Config.Type.keystone_authtoken, "admin_tenant_name").asText();
//
//		String cachePool = null;
//		cacheInitialized = false;
//		memcacheSecurityStrategy = Config.Instance.getOpt(Config.Type.keystone_authtoken, "memcache_security_strategy")
//				.asText();
//		if (!Strings.isNullOrEmpty(memcacheSecurityStrategy)) {
//			memcacheSecurityStrategy = memcacheSecurityStrategy.toUpperCase();
//		}
//
//		memcacheSecretKey = Config.Instance.getOpt(Config.Type.keystone_authtoken, "memcache_secret_key").asText();
//
//		assertValidMemcacheProtectionConfig();
//		int tokenCacheTime = Config.Instance.getOpt(Config.Type.keystone_authtoken, "token_cache_time").asInteger();
//		tokenRevocationList = null;
//		tokenRevocationListFetchedTime = 0;
//		tokenRevocationListCacheTimeout = Config.Instance.getOpt(Config.Type.keystone_authtoken, "revocation_cache_time")
//				.asInteger();
//		int httpConnectTimeout = tokenRevocationListCacheTimeout = Config.Instance.getOpt(Config.Type.keystone_authtoken,
//				"http_connect_timeout").asInteger();
//
//		String authVersion = null;
//		int httpRequestMaxRetries = Config.Instance.getOpt(Config.Type.keystone_authtoken, "http_request_max_retires")
//				.asInteger();
//
//		boolean includeServiceCatalog = Config.Instance.getOpt(Config.Type.keystone_authtoken, "include_service_catalog")
//				.asBoolean();
//
//	}
//
//	private void assertValidMemcacheProtectionConfig() {
//		if (!Strings.isNullOrEmpty(memcacheSecurityStrategy)) {
//			try {
//				MemcaheSecurityStrategy.valueOf(memcacheSecurityStrategy);
//			} catch (Exception e) {
//				throw new IllegalArgumentException("memcache_security_strategy must be ENCRYPT or MAC");
//			}
//		}
//
//		if (Strings.isNullOrEmpty(memcacheSecretKey)) {
//			throw new IllegalArgumentException("memcache_security_key must be defined when a memcache_security_strategy");
//		}
//
//	}
//
//	public void call(ContainerRequestContext env) {
//		logger.debug("Authenticating user token");
//
//		if (cacheInitialized) {
//			initCache(env);
//		}
//
//		try {
//			removeAuthHeaders(env);
//			String userToken = getUsetTokenFromHeader(env);
//			tokenInfo = validateUserToken(userToken, env);
//
//			userHeaders = buildUserHeaders(tokenInfo);
//			addHeaders(env, userHeaders);
//			return app(env, startResponse);
//		} catch (InvalidUserTokenException e) {
//			if(this.delayAuthDecision){
//				logger.info("Invalid user token - deferring reject downstream");
//				addHeaders(env,);
//				return app(env,startResponse);
//			}else{
//				logger.info("Invalid user token -rejecting request");
//				return rejectRequest(enc,startResponse);
//			}
//		}catch(ServiceException e){
//			logger.error("Unable to obtain admin");
//			resp = MiniResp("Service unavailable",env);
//			startResponse("504 Service Unavailable",resp.getHeaders());
//			return resp.getBody();
//		}
//	}
//
//	private Object validateUserToken(String userToken, ContainerRequestContext env) {
//		String tokenId = null;
//
//		try {
//			tokenId = cms.hashToken(userToken);
//			cached = cacheGet(tokenId);
//			if (cached != null) {
//				return cached;
//			}
//			verified = verifySignedToken(userToken);
//			expires = confirmTokenNotExpired(data);
//			confirmTokenBind(data, env);
//			cachePut(tokenId, data, expires);
//			return data;
//
//		} catch (Exception e) {
//			logger.debug("Token validation failure");
//			logger.warn("Authorization failed for token");
//			if (!Strings.isNullOrEmpty(tokenId)) {
//				cacheStoreInvalid(tokenId);
//			}
//			throw new InvalidUserTokenException("Token authorization failed");
//		}
//	}
//
//	private boolean verifySignedToken(String signedText) throws NoSuchAlgorithmException, InvalidUserTokenException,
//			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
//		if (isSignedTokenRevoked(signedText)) {
//			throw new InvalidUserTokenException("Token has been revoked");
//		}
//		String formatted = cms.toCms(signedText);
//		return cmsVerify(formatted);
//	}
//
//	private boolean isSignedTokenRevoked(String signedText) throws NoSuchAlgorithmException, IOException {
//		List<String> revocationList = getTokenRevocationList();
//		if (revocationList.isEmpty()) {
//			return false;
//		}
//		String tokenid = cms.hashToken(signedText);
//
//		if (revocationList.contains(tokenid)) {
//			logger.debug("Token is marked as having been revoked");
//			return true;
//		}
//		return false;
//	}
//
//	private List<String> getTokenRevocationList() throws IOException {
//		int timeout = this.tokenRevocationListFetchedTime + this.tokenRevocationListCacheTimeout;
//		boolean listIsCurrent = System.currentTimeMillis() < timeout;
//		if (listIsCurrent) {
//			if (tokenRevocationList == null) {
//				String text = open(revokedFileName);
//
//			}
//		}
//
//		return null;
//	}
//
//	public void setTokenRevocationListFetchedTime(int value) {
//		this.tokenRevocationListFetchedTime = value;
//	}
//
//	private boolean cmsVerify(String data) {
//		while (true) {
//			try {
//				String output = Cms.Instance.
//			} catch (Exception e) {
//
//			}
//		}
//	}
//
//	private void cacheStoreInvalid(String tokenId) {
//		logger.debug("Marking token as unauthorized in cache");
//		cacheStore(tokenId, "invalid");
//	}
//
//	private String getUsetTokenFromHeader(ContainerRequestContext env) throws InvalidUserTokenException {
//		String tokenId = getHeader(env, "X-Auth-Token", getHeader(env, "X-Storage-Token"));
//		if (!Strings.isNullOrEmpty(tokenId)) {
//			return tokenId;
//		} else {
//			if (!delayAuthDecision) {
//				logger.warn("Unable to find authentication token in headers");
//				logger.debug("Headers: {}", env.getHeaders());
//				throw new InvalidUserTokenException("Unable to find token in headers");
//			}
//		}
//
//		return null;
//	}
//
//	private String getHeader(ContainerRequestContext env, String key, String defaultValue) {
//		String envKey = headerToEnvVar(key);
//		return (env.getProperty(envKey) == null) ? ((env.getProperty(key) == null) ? defaultValue : (String) env
//				.getProperty(key)) : (String) env.getProperty(envKey);
//	}
//
//	private String getHeader(ContainerRequestContext env, String key) {
//		return getHeader(env, key, null);
//	}
//
//	private void removeAuthHeaders(ContainerRequestContext env) {
//		logger.debug("Removing headers from request environment");
//		removeHeaders(env, AUTH_HEADERS);
//	}
//
//	private void removeHeaders(ContainerRequestContext env, List<String> keys) {
//		for (String key : keys) {
//			env.removeProperty(key);
//			String envKey = headerToEnvVar(key);
//			env.removeProperty(envKey);
//		}
//	}
//
//	private String headerToEnvVar(String key) {
//		return "HTTP_" + key.replaceAll("-", "_").toUpperCase();
//	}
//
//	private void initCache(ContainerRequestContext env) {
//		// TODO ignore cahe
//		cacheInitialized = true;
//	}
//
//	private void verifySigningDir(String dirName) {
//		File dir = new File(dirName);
//
//		// if the directory does not exist, create it
//		if (dir.exists() && dir.isDirectory()) {
//			if (!dir.canWrite()) {
//				throw new IllegalStateException("unable to access signing dir: " + dirName);
//			}
//		} else {
//			dir.mkdir();
//		}
//	}
//
//	private String open(String filePath) throws IOException {
//		// File file = new File(filePath);
//		FileReader fileReader = null;
//		BufferedReader bufferedReader = null;
//		String line = null;
//		StringBuffer buffer = new StringBuffer();
//
//		try {
//			fileReader = new FileReader(filePath);
//			bufferedReader = new BufferedReader(fileReader);
//			while ((line = bufferedReader.readLine()) != null) {
//				buffer.append(line);
//			}
//
//		} finally {
//			bufferedReader.close();
//			fileReader.close();
//		}
//
//		return buffer.toString();
//
//	}
// }
