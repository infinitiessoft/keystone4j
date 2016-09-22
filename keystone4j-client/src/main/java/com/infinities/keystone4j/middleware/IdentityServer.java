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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.candlepin.thumbslug.ssl.SslPemException;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.middleware.exception.CertificateConfigException;
import com.infinities.keystone4j.middleware.exception.InvalidUserTokenException;
import com.infinities.keystone4j.middleware.exception.NetworkException;
import com.infinities.keystone4j.middleware.model.Auth;
import com.infinities.keystone4j.middleware.model.PasswordCredentials;
import com.infinities.keystone4j.middleware.model.RevokedToken;
import com.infinities.keystone4j.middleware.model.wrapper.AccessWrapper;
import com.infinities.keystone4j.middleware.model.wrapper.AuthWrapper;
import com.infinities.keystone4j.model.common.Version;
import com.infinities.keystone4j.model.common.VersionsWrapper;
import com.infinities.keystone4j.ssl.CutomSslContextFactory;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;

public class IdentityServer {

	private final static Logger logger = LoggerFactory.getLogger(IdentityServer.class);
	private final static String[] LIST_OF_VERSIONS_TO_ATTEMPT = new String[] { "v3.0", "v2.0" };
	private final boolean includeServiceCatalog;
	private final String reqAuthVersion;
	private String identityUri;
	private String authUri;
	private final String certFile;
	private final String keyFile;
	private final String sslCaFile;
	private final boolean sslInsecure;
	private String adminToken;
	private Calendar adminTokenExpiry;
	private final String adminUser;
	private final String adminPassword;
	private final String adminTenantName;
	private final int httpConnectTimeout;
	private String authVersion;
	private final int httpRequestMaxRetries;

	protected Client client; // for unit testing


	public IdentityServer(boolean includeServiceCatalog, String identity_uri, String auth_uri, String auth_host,
			int auth_port, String auth_protocol, String auth_admin_prefix, String certfile, String keyfile, String cafile,
			boolean insecure, String admin_token, String admin_user, String admin_password, String admin_tenant_name,
			int httpConnectTimeout, int httpRequestMaxRetries, String auth_version) throws MalformedURLException {
		this.includeServiceCatalog = includeServiceCatalog;
		this.reqAuthVersion = auth_version;
		this.identityUri = identity_uri;
		this.authUri = auth_uri;

		if (Strings.isNullOrEmpty(identityUri)) {
			logger.warn("Configuring admin URI using auth fragments. This is deprecated, use \'identity_uri\' instead.");

			// TODO ipv6
			identityUri = String.format("%s://%s:%s", auth_protocol, auth_host, auth_port);

			if (!Strings.isNullOrEmpty(auth_admin_prefix)) {
				identityUri = String.format("%s/%s", identityUri, auth_admin_prefix.replaceAll("^/+|/+$", ""));
			}
		} else {
			identityUri = identityUri.replaceAll("/+$", "");
		}

		if (Strings.isNullOrEmpty(authUri)) {
			logger.warn("Configuring auth_uri to point to the public identity endpoint is required; clients may not be able to authenticate against an admin endpoint");
			logger.debug("auth_url: {}", identityUri);
			authUri = new URL(new URL(identityUri), "/").toString();
			authUri = authUri.replaceAll("/+$", "");
		}
		this.certFile = certfile;
		this.keyFile = keyfile;
		this.sslCaFile = cafile;
		this.sslInsecure = insecure;
		this.adminToken = admin_token;
		if (!Strings.isNullOrEmpty(admin_token)) {
			logger.warn("The admin_token option in the auth_token middleware is deprecated and should not be used. The admin_user and admin_password options should be used instead. The admin_token option may be removed in a future release.");
		}
		this.adminTokenExpiry = null;
		this.adminUser = admin_user;
		this.adminPassword = admin_password;
		this.adminTenantName = admin_tenant_name;
		this.httpConnectTimeout = httpConnectTimeout;
		this.authVersion = null;
		this.httpRequestMaxRetries = httpRequestMaxRetries;
	}

	public String getAuthUri() {
		return authUri;
	}

	public AccessWrapper verifyToken(String userToken, boolean retry) throws SslPemException {

		if (Strings.isNullOrEmpty(authVersion)) {
			authVersion = chooseApiVersion();
		}

		Map<String, String> headers = new HashMap<String, String>();
		Response response = null;
		if ("v3.0".equals(authVersion)) {
			headers.put("X-Auth-Token", getAdminToken());
			headers.put("X-Subject-Token", safeQuote(userToken));

			String path = "/v3/auth/tokens";
			if (!includeServiceCatalog) {
				path = path + "?nocatalog";
			}
			response = jsonRequest(HttpMethod.GET, path, headers);
		} else {
			headers.put("X-Auth-Token", getAdminToken());
			response = jsonRequest("GET", String.format("/v2.0/tokens/%s", safeQuote(userToken)), headers);
		}

		if (response.getStatus() == 200) {
			return response.readEntity(AccessWrapper.class);
		}

		if (response.getStatus() == 404) {
			logger.warn("Authorization failed for token");
			throw new InvalidUserTokenException("Token authorization failed");
		}

		if (response.getStatus() == 401) {
			logger.info("Keystone rejected admin token, resetting");
			this.adminToken = null;
		} else {
			logger.error("Bad response code while validating token: {}", response.getStatus());
		}

		if (retry) {
			logger.info("Retrying validation");
			return verifyToken(userToken, false);
		} else {
			logger.warn("Invalid user token, Keystone response: %s", response.getEntity());
		}

		throw new InvalidUserTokenException();

	}

	public String fetchRevocationList(boolean retry) throws SslPemException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Auth-Token", getAdminToken());
		Response response = jsonRequest("GET", "/v2.0/tokens/revoked", headers);

		if (response.getStatus() == 401) {
			if (retry) {
				logger.info("Keystone rejected admin token, resetting admin token");
				this.adminToken = null;
				return fetchRevocationList(false);
			}
		}

		if (response.getStatus() != 200) {
			throw new ServiceException("Unable to fetch token revocation list.");
		}

		RevokedToken revokedToken = response.readEntity(RevokedToken.class);

		if (Strings.isNullOrEmpty(revokedToken.getSigned())) {
			throw new ServiceException("Revocation list improperly formatted.");
		}

		return revokedToken.getSigned();
	}

	public String fetchSigningCert() throws SslPemException {
		return fetchCertFile("signing");
	}

	public String fetchCaCert() throws SslPemException {
		return fetchCertFile("ca");
	}

	private String fetchCertFile(String certType) throws SslPemException {
		if (Strings.isNullOrEmpty(authVersion)) {
			authVersion = chooseApiVersion();
		}

		String path = null;

		if ("v3.0".equals(authVersion)) {
			if ("signing".equals(certType)) {
				certType = "certificates";
			}
			path = "/v3/OS-SIMPLE-CERT/" + certType;
		} else {
			path = "/v2.0/certificates/" + certType;
		}
		Response response = httpRequest(HttpMethod.GET, path, null);
		String text = response.readEntity(String.class);
		if (response.getStatus() != 200) {
			throw new CertificateConfigException(text);
		}
		return text;
	}

	private String safeQuote(String userToken) {
		try {
			if (userToken.equals(URLDecoder.decode(userToken, "UTF-8"))) {
				// no encoded
				return URLEncoder.encode(userToken, "UTF-8");
			} else {
				// encoded
				return userToken;
			}
		} catch (Exception e) {
			// no encoded
			try {
				return URLEncoder.encode(userToken, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				logger.warn("encode token failed", e);
				throw new ServiceException(e);
			}
		}
	}

	private String getAdminToken() throws SslPemException {
		if (adminTokenExpiry != null) {
			if (willExpireSoon(adminTokenExpiry)) {
				adminToken = null;
			}
		}

		if (Strings.isNullOrEmpty(adminToken)) {
			Entry<String, Calendar> entry = requestAdminToken();
			adminToken = entry.getKey();
			adminTokenExpiry = entry.getValue();
		}

		return adminToken;
	}

	private boolean willExpireSoon(Calendar expiry) {
		Calendar soon = Calendar.getInstance();
		soon.add(Calendar.SECOND, 30);

		return soon.after(expiry);
	}

	private Entry<String, Calendar> requestAdminToken() throws SslPemException {
		PasswordCredentials credentials = new PasswordCredentials();
		credentials.setUsername(adminUser);
		credentials.setPassword(adminPassword);
		Auth auth = new Auth();
		auth.setTenantName(adminTenantName);
		auth.setPasswordCredentials(credentials);
		AuthWrapper authWrapper = new AuthWrapper();
		authWrapper.setAuth(auth);

		try {
			String authStr = JsonUtils.toString(authWrapper);
			logger.debug("request admin token by auth:{}", authStr);
		} catch (IOException e1) {
			logger.warn("conver authWrapper failed", e1);
		}

		Response response = jsonRequest(HttpMethod.POST, "/v2.0/tokens", authWrapper, null);

		try {
			AccessWrapper tokenWrapper = response.readEntity(AccessWrapper.class);
			String token = tokenWrapper.getAccess().getToken().getId();
			Calendar expiry = tokenWrapper.getAccess().getToken().getExpires();
			Entry<String, Calendar> entry = Maps.immutableEntry(token, expiry);
			return entry;
		} catch (Exception e) {
			logger.error("request admin token fail", e);
			logger.warn("Unexpected response from keystone service: {}", response.getStatus());
			throw new ServiceException("invalid json response");
		}
	}

	private String chooseApiVersion() throws SslPemException {
		String versionToUse = null;
		if (!Strings.isNullOrEmpty(reqAuthVersion)) {
			versionToUse = reqAuthVersion;
			logger.info("Auth Token proceeding with prequested {} apis", versionToUse);
		} else {
			versionToUse = null;
			Set<String> versionsSupportedByServer = getSupportedVersions();
			if (versionsSupportedByServer != null && !versionsSupportedByServer.isEmpty()) {
				for (String version : LIST_OF_VERSIONS_TO_ATTEMPT) {
					if (versionsSupportedByServer.contains(version)) {
						versionToUse = version;
						break;
					}
				}
			}

			if (!Strings.isNullOrEmpty(versionToUse)) {
				logger.info("Auth Token confirmed use of {} apis", versionToUse);
			} else {
				logger.error("Attempted versions {} not in list supported by server {}", new Object[] {
						LIST_OF_VERSIONS_TO_ATTEMPT, versionsSupportedByServer });
				throw new ServiceException("no compatible apis supported by server");
			}
		}
		return versionToUse;
	}

	private Set<String> getSupportedVersions() throws SslPemException {
		Set<String> versions = new HashSet<String>();
		Response response = jsonRequest(HttpMethod.GET, "/", null);
		if (response.getStatus() == 501) {
			logger.warn("Old keystone installation found...assuming v2.0");
			versions.add("v2.0");
		} else if (response.getStatus() != 300) {
			logger.error("Unable to get version info from keystone: {}", response.getStatus());
			throw new ServiceException("Unable to get version info from keystone");
		} else {
			try {
				VersionsWrapper versionWrapper = response.readEntity(VersionsWrapper.class);
				for (Version version : versionWrapper.getVersions().getValues()) {
					versions.add(version.getId());
				}
			} catch (Exception e) {
				logger.error("Invalid version response format from server", e);
				throw new ServiceException("Unable to parse version response from keystone");
			}
		}
		logger.debug("Server reports support for api version: {}", versions);
		return versions;
	}

	private Response jsonRequest(String method, String path, Map<String, String> headers) throws SslPemException {
		return jsonRequest(method, path, null, headers);
	}

	private Response jsonRequest(String method, String path, Object body, Map<String, String> headers)
			throws SslPemException {
		Map<String, String> kwargs = new HashMap<String, String>();
		if (headers != null && !headers.isEmpty()) {
			kwargs.putAll(headers);
		}

		Client client = getClient();
		logger.debug("identityUri:{}, path:{}, body:{}", new Object[] { identityUri, path, String.valueOf(body != null) });
		Builder builder = client.target(identityUri).path(path.replaceAll("/+$", ""))
				.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE);

		for (Entry<String, String> header : kwargs.entrySet()) {
			builder.header(header.getKey(), header.getValue());
		}

		if (body != null) {
			return httpRequest(builder, method, Entity.entity(body, MediaType.APPLICATION_JSON));
		} else {
			return httpRequest(builder, method, null);
		}
	}

	private Response httpRequest(String method, String path, Object body) throws SslPemException {
		Client client = getClient();
		Builder builder = client.target(identityUri).path(path.replaceAll("/+$", "")).request();
		if (body != null) {
			return httpRequest(builder, method, Entity.text(body));
		} else {
			return httpRequest(builder, method, null);
		}
	}

	private Response httpRequest(Builder builder, String method, Entity<?> entity) {
		int maxRetries = this.httpRequestMaxRetries;
		int retry = 0;
		while (true) {
			try {
				if (entity != null) {
					return builder.method(method, entity);
				} else {
					return builder.method(method);
				}
			} catch (Exception e) {
				if (retry >= maxRetries) {
					logger.error("HTTP connection exception", e);
					throw new NetworkException("unable to communicate with keystone");
				}
				logger.warn("Retrying on HTTP Connection exception", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				retry += 1;
			}
		}
	}

	private Client getClient() throws SslPemException {
		if (client != null) {
			return client;
		}

		// ClientConfig configuration = new ClientConfig();
		// configuration.property(ClientProperties.CONNECT_TIMEOUT,
		// httpConnectTimeout);
		ClientBuilder builder = ClientBuilder.newBuilder();

		// Client client = ClientBuilder.newClient(configuration);

		if (!Strings.isNullOrEmpty(certFile) && !Strings.isNullOrEmpty(keyFile)) {
			SSLContext sslContext = null;
			if (Strings.isNullOrEmpty(this.sslCaFile)) {
				sslContext = CutomSslContextFactory.getClientContext(certFile, keyFile);
			} else {
				sslContext = CutomSslContextFactory.getClientContext(certFile, keyFile, sslCaFile);
			}
			builder = builder.sslContext(sslContext);
		}

		if (sslInsecure) {
			builder = builder.hostnameVerifier(getHostNameVerifier());
		}

		if (Strings.isNullOrEmpty(this.sslCaFile)) {
			builder = builder.hostnameVerifier(getHostNameVerifier());
		}

		return builder.build().register(JacksonFeature.class).property(ClientProperties.CONNECT_TIMEOUT, httpConnectTimeout)
				.property(ClientProperties.READ_TIMEOUT, httpConnectTimeout);
	}

	private static HostnameVerifier getHostNameVerifier() {
		return new HostnameVerifier() {

			@Override
			public boolean verify(final String hostname, final SSLSession sslSession) {
				return true;
			}
		};
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
