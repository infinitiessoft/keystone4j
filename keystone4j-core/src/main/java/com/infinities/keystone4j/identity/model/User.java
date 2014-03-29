package com.infinities.keystone4j.identity.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Strings;
import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.model.Trust;

@Entity
@Table(name = "USER", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DOMAINID", "NAME" }) })
public class User extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private String email;
	private Domain domain;
	// TODO filter
	private String password;
	private Boolean enabled = true;
	// TODO filter
	private String extra;
	private Project default_project;
	// TODO filter
	private Set<UserGroupMembership> userGroupMemberships = new HashSet<UserGroupMembership>(0);
	// TODO filter
	private Set<UserProjectGrant> userProjectGrants = new HashSet<UserProjectGrant>(0);
	// TODO filter
	private Set<UserDomainGrant> userDomainGrants = new HashSet<UserDomainGrant>(0);
	private Set<Credential> credentials = new HashSet<Credential>(0);
	private Set<Trust> trustsOwn = new HashSet<Trust>(0);
	private Set<Trust> trustsProvide = new HashSet<Trust>(0);
	private Set<Token> tokens = new HashSet<Token>(0);

	private boolean nameUpdated = false;
	private boolean emailUpdated = false;
	private boolean domainUpdated = false;
	private boolean passwordUpdated = false;
	private boolean enabledUpdated = false;
	private boolean extraUpdated = false;
	private boolean defaultProjectUpdated = false;


	// private Set<Assignment> assignments = new HashSet<Assignment>(0);

	@Column(name = "NAME", length = 255, nullable = false)
	@JsonView(Views.Basic.class)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

	@Column(name = "EMAIL", length = 255, nullable = false)
	@JsonView(Views.Basic.class)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		emailUpdated = true;
	}

	@Override
	@JsonView(Views.AuthenticateForToken.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
		domainUpdated = true;
	}

	@Transient
	@XmlElement(name = "domain_id")
	public String getDomainid() {
		if (getDomain() != null) {
			return getDomain().getId();
		}
		return null;
	}

	@Transient
	@XmlElement(name = "domain_id")
	public void setDomainid(String domainid) {
		if (!Strings.isNullOrEmpty(domainid)) {
			Domain domain = new Domain();
			domain.setId(domainid);
			setDomain(domain);
		}
	}

	@Column(name = "PASSWORD", length = 128)
	@JsonView(Views.Advance.class)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		passwordUpdated = true;
	}

	@JsonView(Views.All.class)
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
	public Project getDefault_project() {
		return default_project;
	}

	@XmlTransient
	public void setDefault_project(Project default_project) {
		this.default_project = default_project;
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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<UserProjectGrant> getUserProjectGrants() {
		return userProjectGrants;
	}

	public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants) {
		this.userProjectGrants = userProjectGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<UserDomainGrant> getUserDomainGrants() {
		return userDomainGrants;
	}

	public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
		this.userDomainGrants = userDomainGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustee", cascade = CascadeType.ALL)
	public Set<Trust> getTrustsOwn() {
		return trustsOwn;
	}

	public void setTrustsOwn(Set<Trust> trustsOwn) {
		this.trustsOwn = trustsOwn;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustor", cascade = CascadeType.ALL)
	public Set<Trust> getTrustsProvide() {
		return trustsProvide;
	}

	public void setTrustsProvide(Set<Trust> trustsProvide) {
		this.trustsProvide = trustsProvide;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}

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

	@XmlTransient
	@Transient
	public boolean isEmailUpdated() {
		return emailUpdated;
	}

	@XmlTransient
	@Transient
	public void setEmailUpdated(boolean emailUpdated) {
		this.emailUpdated = emailUpdated;
	}

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

	@XmlTransient
	@Transient
	@Override
	@JsonIgnore
	public Project getProject() {
		return default_project;
	}

	@Transient
	@Override
	@XmlTransient
	@JsonIgnore
	public User getUser() {
		throw new IllegalStateException("propert 'user' not exist");
	}

	@Transient
	@XmlElement(name = "default_project_id")
	public String getDefaultProjectId() {
		if (getDefault_project() == null) {
			return null;
		} else {
			return getDefault_project().getId();
		}
	}

	@Transient
	@XmlElement(name = "default_project_id")
	public void setDefaultProjectId(String defaultProjectId) {
		Project project = new Project();
		project.setId(defaultProjectId);
		setDefault_project(project);
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
