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
package com.infinities.keystone4j.contrib.revoke.model;

import java.util.Calendar;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.google.common.base.Strings;

public class RevokeEvent {

	@XmlElement(name = "OS-TRUST:trust_id")
	private String trustId;
	@XmlElement(name = "OS-OAUTH1:consumer_id")
	private String consumerId;
	@XmlElement(name = "OS-OAUTH1:access_token_id")
	private String accessTokenId;
	@XmlElement(name = "audit_id")
	private String auditId;
	@XmlElement(name = "audit_chain_id")
	private String auditChainId;
	@XmlElement(name = "expires_at")
	private Calendar expiresAt;
	@XmlElement(name = "domain_id")
	private String domainId;
	@XmlElement(name = "project_id")
	private String projectId;
	@XmlElement(name = "user_id")
	private String userId;
	@XmlElement(name = "role_id")
	private String roleId;
	@XmlElement(name = "issued_before")
	private Calendar issuedBefore;
	@XmlElement(name = "revoked_at")
	private Calendar revokedAt;
	@XmlElement(name = "domain_scope_id")
	private String domainScopeId;


	public RevokeEvent() {

	}

	@XmlTransient
	@Transient
	public void init() {
		if (!Strings.isNullOrEmpty(getDomainId()) && getExpiresAt() != null) {
			setDomainScopeId(domainId);
			domainId = null;
		} else {
			setDomainScopeId(null);
		}

		if (revokedAt == null) {
			revokedAt = Calendar.getInstance();
		}

		if (issuedBefore == null) {
			issuedBefore = revokedAt;
		}

	}

	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	public String getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(String accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	public String getAuditChainId() {
		return auditChainId;
	}

	public void setAuditChainId(String auditChainId) {
		this.auditChainId = auditChainId;
	}

	public Calendar getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Calendar expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Calendar getIssuedBefore() {
		return issuedBefore;
	}

	public void setIssuedBefore(Calendar issuedBefore) {
		this.issuedBefore = issuedBefore;
	}

	public Calendar getRevokedAt() {
		return revokedAt;
	}

	public void setRevokedAt(Calendar revokedAt) {
		this.revokedAt = revokedAt;
	}

	public String getDomainScopeId() {
		return domainScopeId;
	}

	public void setDomainScopeId(String domainScopeId) {
		this.domainScopeId = domainScopeId;
	}

}
