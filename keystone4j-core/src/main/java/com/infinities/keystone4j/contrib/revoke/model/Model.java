package com.infinities.keystone4j.contrib.revoke.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenData.Trust;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.TokenV2;

public class Model {

	public static TokenValues buildTokenValues(TokenData tokenData) {
		Calendar tokenExpiresAt = tokenData.getExpireAt();
		TokenValues tokenValues = new TokenValues();
		tokenValues.setExpiresAt(tokenExpiresAt);
		tokenValues.setIssuedAt(tokenData.getIssuedAt());
		String auditId = tokenData.getAuditIds() == null || tokenData.getAuditIds().isEmpty() ? null : tokenData
				.getAuditIds().get(0);
		tokenValues.setAuditId(auditId);
		String auditChainId = tokenData.getAuditIds() == null || tokenData.getAuditIds().isEmpty() ? null : tokenData
				.getAuditIds().get(tokenData.getAuditIds().size() - 1);
		tokenValues.setAuditChainId(auditChainId);

		User user = tokenData.getUser();
		if (user != null) {
			tokenValues.setUserId(user.getId());
			tokenValues.setIdentityDomainId(user.getDomain() == null ? null : user.getDomain().getId());
		} else {
			tokenValues.setUserId(null);
			tokenValues.setIdentityDomainId(null);
		}

		Project project = tokenData.getProject();
		if (project != null) {
			tokenValues.setProjectId(project.getId());
			tokenValues.setAssignmentDomainId(project.getDomain().getId());
		} else {
			tokenValues.setProjectId(null);

			Domain domain = tokenData.getDomain();
			if (domain != null) {
				tokenValues.setAssignmentDomainId(domain.getId());
			} else {
				tokenValues.setAssignmentDomainId(null);
			}
		}

		List<String> roleList = new ArrayList<String>();
		List<Role> roles = tokenData.getRoles();
		if (roles != null && !roles.isEmpty()) {
			for (Role role : roles) {
				roleList.add(role.getId());
			}
		}
		tokenValues.setRoles(roleList);

		Trust trust = tokenData.getTrust();
		if (trust == null) {
			tokenValues.setTrustId(null);
			tokenValues.setTrustorId(null);
			tokenValues.setTrusteeId(null);
		} else {
			tokenValues.setTrusteeId(trust.getId());
			tokenValues.setTrustorId(trust.getTrustorUser().getId());
			tokenValues.setTrusteeId(trust.getTrusteeUser().getId());
		}

		// TODO ignore oauth1;

		return tokenValues;
	}

	public static TokenValues buildTokenValuesV2(Access access, String defaultDomainId) {
		TokenV2 tokenData = access.getToken();
		Calendar tokenExpiresAt = tokenData.getExpires();
		TokenValues tokenValues = new TokenValues();
		tokenValues.setExpiresAt(tokenExpiresAt);
		tokenValues.setIssuedAt(tokenData.getIssued_at());
		String auditId = tokenData.getAuditIds() == null || tokenData.getAuditIds().isEmpty() ? null : tokenData
				.getAuditIds().get(0);
		tokenValues.setAuditId(auditId);
		String auditChainId = tokenData.getAuditIds() == null || tokenData.getAuditIds().isEmpty() ? null : tokenData
				.getAuditIds().get(tokenData.getAuditIds().size() - 1);
		tokenValues.setAuditChainId(auditChainId);

		tokenValues.setUserId(access.getUser() == null ? null : access.getUser().getId());
		Project project = tokenData.getTenant();
		if (project != null) {
			tokenValues.setProjectId(project.getId());
		} else {
			tokenValues.setProjectId(null);
		}

		tokenValues.setIdentityDomainId(defaultDomainId);
		tokenValues.setAssignmentDomainId(defaultDomainId);

		com.infinities.keystone4j.model.token.v2.TokenV2.Trust trust = tokenData.getTrust();

		if (trust == null) {
			tokenValues.setTrustId(null);
			tokenValues.setTrustorId(null);
			tokenValues.setTrusteeId(null);
		} else {
			tokenValues.setTrustId(trust.getId());
			tokenValues.setTrustorId(trust.getTrustorId());
			tokenValues.setTrusteeId(trust.getTrusteeId());
		}

		List<String> roleList = new ArrayList<String>();

		if (access.getMetadata() != null && access.getMetadata().getRoles() != null) {
			roleList.addAll(access.getMetadata().getRoles());
		}

		tokenValues.setRoles(roleList);
		return tokenValues;
	}


	public static class TokenValues {

		private Calendar expiresAt;
		private Calendar issuedAt;
		private String auditId;
		private String auditChainId;
		private String userId;
		private String identityDomainId;
		private String projectId;
		private String assignmentDomainId;
		private List<String> roles = new ArrayList<String>(0);
		private String trustId;
		private String trustorId;
		private String trusteeId;
		private String consumerId;
		private String accessTokenId;


		public Calendar getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Calendar expiresAt) {
			this.expiresAt = expiresAt;
		}

		public Calendar getIssuedAt() {
			return issuedAt;
		}

		public void setIssuedAt(Calendar issuedAt) {
			this.issuedAt = issuedAt;
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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getIdentityDomainId() {
			return identityDomainId;
		}

		public void setIdentityDomainId(String identityDomainId) {
			this.identityDomainId = identityDomainId;
		}

		public String getProjectId() {
			return projectId;
		}

		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}

		public String getAssignmentDomainId() {
			return assignmentDomainId;
		}

		public void setAssignmentDomainId(String assignmentDomainId) {
			this.assignmentDomainId = assignmentDomainId;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public String getTrustId() {
			return trustId;
		}

		public void setTrustId(String trustId) {
			this.trustId = trustId;
		}

		public String getTrustorId() {
			return trustorId;
		}

		public void setTrustorId(String trustorId) {
			this.trustorId = trustorId;
		}

		public String getTrusteeId() {
			return trusteeId;
		}

		public void setTrusteeId(String trusteeId) {
			this.trusteeId = trusteeId;
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

	}

}
