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
package com.infinities.keystone4j.model.identity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.DomainAwared;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "USERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "DOMAINID", "NAME" }) })
public class User extends BaseEntity implements java.io.Serializable, DomainAwared, IUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	private Domain domain; // keystone.identity.backends.sql.User 20150114
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	// private String email;
	// TODO filter
	private String password;
	private Boolean enabled = true;
	// TODO filter
	private String extra;
	// @XmlElement(name = "default_project")
	private Project defaultProject;
	// TODO filter
	private Set<UserGroupMembership> userGroupMemberships = new HashSet<UserGroupMembership>(0);
	// TODO filter
	// private Set<UserProjectGrant> userProjectGrants = new
	// HashSet<UserProjectGrant>(0);
	// TODO filter
	// private Set<UserDomainGrant> userDomainGrants = new
	// HashSet<UserDomainGrant>(0);
	// private Set<Credential> credentials = new HashSet<Credential>(0);
	// private final Set<Trust> trustsOwn = new HashSet<Trust>(0);
	// private final Set<Trust> trustsProvide = new HashSet<Trust>(0);
	// private Set<Token> tokens = new HashSet<Token>(0);

	private boolean nameUpdated = false;
	// private boolean emailUpdated = false;
	private boolean domainUpdated = false;
	private boolean passwordUpdated = false;
	private boolean enabledUpdated = false;
	private boolean extraUpdated = false;
	private boolean defaultProjectUpdated = false;

	private String tenantId;
	private String username;

	private String firstName;
	private String lastName;
	private boolean firstNameUpdated = false;
	private boolean lastNameUpdated = false;
	private String ip;
	private Integer port;

	private boolean ipUpdated = false;
	private boolean portUpdated = false;


	// private Set<Assignment> assignments = new HashSet<Assignment>(0);

	@Override
	@Column(name = "NAME", length = 255, nullable = false)
	@JsonView(Views.Basic.class)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

	// @Column(name = "EMAIL", length = 255, nullable = false)
	// @JsonView(Views.Basic.class)
	// public String getEmail() {
	// return email;
	// }
	//
	// public void setEmail(String email) {
	// this.email = email;
	// emailUpdated = true;
	// }

	// listUser
	@Override
	@JsonView(Views.AuthenticateForToken.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	@Override
	public void setDomain(Domain domain) {
		this.domain = domain;
		domainUpdated = true;
	}

	@JsonView(Views.Advance.class)
	@Transient
	@Override
	@XmlElement(name = "domain_id")
	public String getDomainId() {
		if (getDomain() != null) {
			return getDomain().getId();
		}
		return null;
	}

	@Override
	@Transient
	@XmlElement(name = "domain_id")
	public void setDomainId(String domainid) {
		if (!(domainid == null || domainid.length() == 0)) {
			Domain domain = new Domain();
			domain.setId(domainid);
			setDomain(domain);
		}
	}

	@JsonView(Views.All.class)
	@Column(name = "PASSWORD", length = 128)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		passwordUpdated = true;
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
	@Column(name = "EXTRA")
	@JsonView(Views.All.class)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFAULTPROJECTID", nullable = false)
	public Project getDefaultProject() {
		return defaultProject;
	}

	@XmlTransient
	public void setDefaultProject(Project defaultProject) {
		this.defaultProject = defaultProject;
		defaultProjectUpdated = true;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@JsonView(Views.All.class)
	public Set<UserGroupMembership> getUserGroupMemberships() {
		return userGroupMemberships;
	}

	public void setUserGroupMemberships(Set<UserGroupMembership> userGroupMemberships) {
		this.userGroupMemberships = userGroupMemberships;
	}

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade =
	// CascadeType.ALL)
	// public Set<UserProjectGrant> getUserProjectGrants() {
	// return userProjectGrants;
	// }
	//
	// public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants)
	// {
	// this.userProjectGrants = userProjectGrants;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade =
	// CascadeType.ALL)
	// public Set<UserDomainGrant> getUserDomainGrants() {
	// return userDomainGrants;
	// }
	//
	// public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
	// this.userDomainGrants = userDomainGrants;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade =
	// CascadeType.ALL)
	// public Set<Credential> getCredentials() {
	// return credentials;
	// }
	//
	// public void setCredentials(Set<Credential> credentials) {
	// this.credentials = credentials;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "trustee", cascade =
	// CascadeType.ALL)
	// public Set<Trust> getTrustsOwn() {
	// return trustsOwn;
	// }
	//
	// public void setTrustsOwn(Set<Trust> trustsOwn) {
	// this.trustsOwn = trustsOwn;
	// }
	//
	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "trustor", cascade =
	// CascadeType.ALL)
	// public Set<Trust> getTrustsProvide() {
	// return trustsProvide;
	// }
	//
	// public void setTrustsProvide(Set<Trust> trustsProvide) {
	// this.trustsProvide = trustsProvide;
	// }

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade =
	// CascadeType.ALL)
	// public Set<Token> getTokens() {
	// return tokens;
	// }
	//
	// public void setTokens(Set<Token> tokens) {
	// this.tokens = tokens;
	// }

	@XmlTransient
	@Transient
	public boolean isNameUpdated() {
		return nameUpdated;
	}

	@XmlTransient
	@Transient
	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	// @XmlTransient
	// @Transient
	// public boolean isEmailUpdated() {
	// return emailUpdated;
	// }
	//
	// @XmlTransient
	// @Transient
	// public void setEmailUpdated(boolean emailUpdated) {
	// this.emailUpdated = emailUpdated;
	// }

	@XmlTransient
	@Transient
	public boolean isDomainUpdated() {
		return domainUpdated;
	}

	@XmlTransient
	@Transient
	public void setDomainUpdated(boolean domainUpdated) {
		this.domainUpdated = domainUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isPasswordUpdated() {
		return passwordUpdated;
	}

	@XmlTransient
	@Transient
	public void setPasswordUpdated(boolean passwordUpdated) {
		this.passwordUpdated = passwordUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isEnabledUpdated() {
		return enabledUpdated;
	}

	@XmlTransient
	@Transient
	public void setEnabledUpdated(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	@XmlTransient
	@Transient
	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isDefaultProjectUpdated() {
		return defaultProjectUpdated;
	}

	@XmlTransient
	@Transient
	public void setDefaultProjectUpdated(boolean defaultProjectUpdated) {
		this.defaultProjectUpdated = defaultProjectUpdated;
	}

	// listUser
	@JsonView(Views.Advance.class)
	@Transient
	@XmlElement(name = "default_project_id")
	public String getDefaultProjectId() {
		if (getDefaultProject() == null) {
			return null;
		} else {
			return getDefaultProject().getId();
		}
	}

	@Transient
	@XmlElement(name = "default_project_id")
	public void setDefaultProjectId(String defaultProjectId) {
		Project project = new Project();
		project.setId(defaultProjectId);
		setDefaultProject(project);
	}

	@Transient
	public String getTenantId() {
		return tenantId;
	}

	@JsonView(Views.Advance.class)
	@Transient
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@JsonView(Views.Advance.class)
	@Transient
	public String getUsername() {
		return username;
	}

	@Transient
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", domain=" + domain + ", password=" + password + ", enabled=" + enabled + ", extra="
				+ extra + ", defaultProject=" + defaultProject + ", userGroupMemberships=" + userGroupMemberships
				+ ", tenantId=" + tenantId + ", username=" + username + "]";
	}

	@Column(name = "FIRSTNAME", length = 50, nullable = true)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstNameUpdated = true;
		this.firstName = firstName;
	}

	@Column(name = "LASTNAME", length = 50, nullable = true)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastNameUpdated = true;
		this.lastName = lastName;
	}

	@XmlTransient
	@Transient
	public boolean isFirstNameUpdated() {
		return firstNameUpdated;
	}

	public void setFirstNameUpdated(boolean firstNameUpdated) {
		this.firstNameUpdated = firstNameUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isLastNameUpdated() {
		return lastNameUpdated;
	}

	public void setLastNameUpdated(boolean lastNameUpdated) {
		this.lastNameUpdated = lastNameUpdated;
	}

	@Column(name = "ip", length = 50, nullable = true)
	@JsonView(Views.Basic.class)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ipUpdated = true;
		this.ip = ip;
	}

	@Column(name = "port", nullable = true)
	@JsonView(Views.Basic.class)
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.portUpdated = true;
		this.port = port;
	}

	@XmlTransient
	@Transient
	public boolean isIpUpdated() {
		return ipUpdated;
	}

	public void setIpUpdated(boolean ipUpdated) {
		this.ipUpdated = ipUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isPortUpdated() {
		return portUpdated;
	}

	public void setPortUpdated(boolean portUpdated) {
		this.portUpdated = portUpdated;
	}

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }

}
