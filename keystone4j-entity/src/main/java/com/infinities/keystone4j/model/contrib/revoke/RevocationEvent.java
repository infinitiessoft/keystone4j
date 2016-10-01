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
package com.infinities.keystone4j.model.contrib.revoke;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "REVOCATION_EVENT")
public class RevocationEvent implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private String id;


	public RevocationEvent() {

	}

	@Id
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TRUST_ID", length = 64)
	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	@Column(name = "CONSUMER_ID", length = 64)
	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	@Column(name = "ACCESS_TOKEN_ID", length = 64)
	public String getAccessTokenId() {
		return accessTokenId;
	}

	public void setAccessTokenId(String accessTokenId) {
		this.accessTokenId = accessTokenId;
	}

	@Column(name = "AUDIT_ID", length = 64)
	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}

	@Column(name = "AUDIT_CHAIN_ID", length = 64)
	public String getAuditChainId() {
		return auditChainId;
	}

	public void setAuditChainId(String auditChainId) {
		this.auditChainId = auditChainId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRES_AT")
	public Calendar getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Calendar expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Column(name = "DOMAIN_ID", length = 64)
	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	@Column(name = "PROJECT_ID", length = 64)
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Column(name = "USER_ID", length = 64)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_ID", length = 64)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ISSUED_BEFORE", nullable = false)
	public Calendar getIssuedBefore() {
		return issuedBefore;
	}

	public void setIssuedBefore(Calendar issuedBefore) {
		this.issuedBefore = issuedBefore;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REVOKED_AT", nullable = false)
	public Calendar getRevokedAt() {
		return revokedAt;
	}

	public void setRevokedAt(Calendar revokedAt) {
		this.revokedAt = revokedAt;
	}

	@Transient
	public String getDomainScopeId() {
		return domainScopeId;
	}

	@Transient
	public void setDomainScopeId(String domainScopeId) {
		this.domainScopeId = domainScopeId;
	}
}
