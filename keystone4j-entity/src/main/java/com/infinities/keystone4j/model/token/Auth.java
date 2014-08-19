package com.infinities.keystone4j.model.token;

public class Auth {

	private PasswordCredentials passwordCredentials;
	private Token token;
	private String trustId;
	private String tenantName;
	private String tenantId;


	public PasswordCredentials getPasswordCredentials() {
		return passwordCredentials;
	}

	public void setPasswordCredentials(PasswordCredentials passwordCredentials) {
		this.passwordCredentials = passwordCredentials;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
