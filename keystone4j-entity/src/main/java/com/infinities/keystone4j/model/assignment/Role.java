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
package com.infinities.keystone4j.model.assignment;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "ROLE", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class Role extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1497968571650652616L;
	@JsonView(Views.Basic.class)
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private String extra;

	private Set<RoleAssignment> roleAssignments = new HashSet<RoleAssignment>(0); // 20150107
	// private Set<GroupProjectGrant> groupProjectGrants = new
	// HashSet<GroupProjectGrant>(0);
	// private Set<GroupDomainGrant> groupDomainGrants = new
	// HashSet<GroupDomainGrant>(0);
	// private Set<UserProjectGrant> userProjectGrants = new
	// HashSet<UserProjectGrant>(0);
	// private Set<UserDomainGrant> userDomainGrants = new
	// HashSet<UserDomainGrant>(0);
	// private final Set<TrustRole> trustRoles = new HashSet<TrustRole>(0);
	// private Set<TokenRole> tokenRoles = new HashSet<TokenRole>(0);

	// transient property
	private String inheritedTo;
	private boolean nameUpdated = false;
	private boolean extraUpdated = false;


	@Column(name = "NAME", length = 255, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setNameUpdated(true);
	}

	@XmlTransient
	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	@XmlTransient
	public void setExtra(String extra) {
		this.extra = extra;
		setExtraUpdated(true);
	}

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<TrustRole> getTrustRoles() {
	// return trustRoles;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setTrustRoles(Set<TrustRole> trustRoles) {
	// this.trustRoles = trustRoles;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<GroupProjectGrant> getGroupProjectGrants() {
	// return groupProjectGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setGroupProjectGrants(Set<GroupProjectGrant>
	// groupProjectGrants) {
	// this.groupProjectGrants = groupProjectGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<GroupDomainGrant> getGroupDomainGrants() {
	// return groupDomainGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setGroupDomainGrants(Set<GroupDomainGrant> groupDomainGrants)
	// {
	// this.groupDomainGrants = groupDomainGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<UserProjectGrant> getUserProjectGrants() {
	// return userProjectGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants)
	// {
	// this.userProjectGrants = userProjectGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<UserDomainGrant> getUserDomainGrants() {
	// return userDomainGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
	// this.userDomainGrants = userDomainGrants;
	// }

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }

	// @Column(name = "INHERITEDTO", nullable = false)
	@Transient
	@XmlTransient
	public String getInheritedTo() {
		return inheritedTo;
	}

	@Transient
	@XmlTransient
	public void setInheritedTo(String inheritedTo) {
		this.inheritedTo = inheritedTo;
	}

	@Transient
	@XmlTransient
	public boolean isNameUpdated() {
		return nameUpdated;
	}

	@Transient
	@XmlTransient
	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	@Transient
	@XmlTransient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	@Transient
	@XmlTransient
	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade =
	// CascadeType.ALL)
	// public Set<TokenRole> getTokenRoles() {
	// return tokenRoles;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setTokenRoles(Set<TokenRole> tokenRoles) {
	// this.tokenRoles = tokenRoles;
	// }

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<RoleAssignment> getRoleAssignments() {
		return roleAssignments;
	}

	public void setRoleAssignments(Set<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}
}
