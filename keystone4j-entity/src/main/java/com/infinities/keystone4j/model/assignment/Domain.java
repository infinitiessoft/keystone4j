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
@Table(name = "DOMAIN", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }) })
public class Domain extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4125628352361369176L;

	@JsonView(Views.Basic.class)
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private Boolean enabled = true;
	private String extra;
	private Set<Project> projects = new HashSet<Project>(0); // assignment.bakcend.sql.Project
	// private Set<Group> groups = new HashSet<Group>(0);
	// private Set<User> users = new HashSet<User>(0);
	// private Set<Token> tokens = new HashSet<Token>(0);
	// private Set<UserDomainGrant> userDomainGrants = new
	// HashSet<UserDomainGrant>(0);
	// private Set<GroupDomainGrant> groupDomainGrants = new
	// HashSet<GroupDomainGrant>(0);
	// private Set<Assignment> assignments = new HashSet<Assignment>(0);
	private boolean nameUpdated = false;
	private boolean enabledUpdated = false;
	private boolean extraUpdated = false;


	@JsonView(Views.Basic.class)
	@Column(name = "NAME", length = 64, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

	@JsonView(Views.Advance.class)
	@Column(name = "ENABLED", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		enabledUpdated = true;
	}

	@XmlTransient
	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<UserDomainGrant> getUserDomainGrants() {
	// return userDomainGrants;
	// }
	//
	// public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
	// this.userDomainGrants = userDomainGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<GroupDomainGrant> getGroupDomainGrants() {
	// return groupDomainGrants;
	// }
	//
	// public void setGroupDomainGrants(Set<GroupDomainGrant> groupDomainGrants)
	// {
	// this.groupDomainGrants = groupDomainGrants;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<Group> getGroups() {
	// return groups;
	// }
	//
	// public void setGroups(Set<Group> groups) {
	// this.groups = groups;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<User> getUsers() {
	// return users;
	// }
	//
	// public void setUsers(Set<User> users) {
	// this.users = users;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<Token> getTokens() {
	// return tokens;
	// }
	//
	// public void setTokens(Set<Token> tokens) {
	// this.tokens = tokens;
	// }

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }
	@XmlTransient
	@Transient
	public boolean isNameUpdated() {
		return nameUpdated;
	}

	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isEnabledUpdated() {
		return enabledUpdated;
	}

	public void setEnabledUpdated(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

}