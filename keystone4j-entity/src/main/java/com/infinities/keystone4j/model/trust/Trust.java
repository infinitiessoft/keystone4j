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
package com.infinities.keystone4j.model.trust;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.common.CollectionLinks;

@Entity
@Table(name = "TRUST", schema = "PUBLIC", catalog = "PUBLIC")
public class Trust extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3573037726561198048L;
	@XmlElement(name = "trustor_user_id")
	private String trustorUserId;
	@XmlElement(name = "trustee_user_id")
	private String trusteeUserId;
	@XmlElement(name = "project_id")
	private String projectId;
	private Boolean impersonation;
	private Calendar deletedAt;
	private Calendar expiresAt;
	private String extra;
	@XmlElement(name = "remaining_uses")
	private Integer remainingUses;

	// private final Set<Token> tokens = new HashSet<Token>(0);

	private List<Role> roles = new ArrayList<Role>();


	@Transient
	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}


	@XmlElement(name = "roles_links")
	private CollectionLinks rolesLinks = new CollectionLinks();


	@Column(name = "IMPERSONATION", nullable = false)
	public Boolean getImpersonation() {
		return impersonation;
	}

	public void setImpersonation(Boolean impersonation) {
		this.impersonation = impersonation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETEAT")
	public Calendar getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Calendar deletedAt) {
		this.deletedAt = deletedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIREAT")
	public Calendar getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Calendar expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "trust", cascade =
	// CascadeType.ALL)
	// public Set<Token> getTokens() {
	// return tokens;
	// }
	//
	// public void setTokens(Set<Token> tokens) {
	// this.tokens = tokens;
	// }

	@Transient
	public CollectionLinks getRolesLinks() {
		return rolesLinks;
	}

	@Transient
	public void setRolesLinks(CollectionLinks rolesLinks) {
		this.rolesLinks = rolesLinks;
	}

	@XmlElement(name = "remaining_uses")
	@Column(name = "REMAINING_USE", nullable = false)
	public Integer getRemainingUses() {
		return remainingUses;
	}

	public void setRemainingUses(Integer remainingUses) {
		this.remainingUses = remainingUses;
	}

	@Column(name = "TRUSTOR_USER_ID", length = 64, nullable = false)
	public String getTrustorUserId() {
		return trustorUserId;
	}

	public void setTrustorUserId(String trustorUserId) {
		this.trustorUserId = trustorUserId;
	}

	@Column(name = "TRUSTEE_USER_ID", length = 64, nullable = false)
	public String getTrusteeUserId() {
		return trusteeUserId;
	}

	public void setTrusteeUserId(String trusteeUserId) {
		this.trusteeUserId = trusteeUserId;
	}

	@Column(name = "PROJECT_ID", length = 64)
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
