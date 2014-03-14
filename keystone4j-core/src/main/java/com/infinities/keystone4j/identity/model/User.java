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
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.model.Trust;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
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
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

	@Column(name = "EMAIL", length = 255, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		emailUpdated = true;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
		domainUpdated = true;
	}

	@Column(name = "PASSWORD", length = 128)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		passwordUpdated = true;
	}

	@Column(name = "ENABLED", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		enabledUpdated = true;
	}

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFAULTPROJECTID", nullable = false)
	public Project getDefault_project() {
		return default_project;
	}

	public void setDefault_project(Project default_project) {
		this.default_project = default_project;
		defaultProjectUpdated = true;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<UserGroupMembership> getUserGroupMemberships() {
		return userGroupMemberships;
	}

	public void setUserGroupMemberships(Set<UserGroupMembership> userGroupMemberships) {
		this.userGroupMemberships = userGroupMemberships;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<UserProjectGrant> getUserProjectGrants() {
		return userProjectGrants;
	}

	public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants) {
		this.userProjectGrants = userProjectGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<UserDomainGrant> getUserDomainGrants() {
		return userDomainGrants;
	}

	public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
		this.userDomainGrants = userDomainGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustee", cascade = CascadeType.ALL)
	public Set<Trust> getTrustsOwn() {
		return trustsOwn;
	}

	public void setTrustsOwn(Set<Trust> trustsOwn) {
		this.trustsOwn = trustsOwn;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustor", cascade = CascadeType.ALL)
	public Set<Trust> getTrustsProvide() {
		return trustsProvide;
	}

	public void setTrustsProvide(Set<Trust> trustsProvide) {
		this.trustsProvide = trustsProvide;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}

	public boolean isNameUpdated() {
		return nameUpdated;
	}

	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	public boolean isEmailUpdated() {
		return emailUpdated;
	}

	public void setEmailUpdated(boolean emailUpdated) {
		this.emailUpdated = emailUpdated;
	}

	public boolean isDomainUpdated() {
		return domainUpdated;
	}

	public void setDomainUpdated(boolean domainUpdated) {
		this.domainUpdated = domainUpdated;
	}

	public boolean isPasswordUpdated() {
		return passwordUpdated;
	}

	public void setPasswordUpdated(boolean passwordUpdated) {
		this.passwordUpdated = passwordUpdated;
	}

	public boolean isEnabledUpdated() {
		return enabledUpdated;
	}

	public void setEnabledUpdated(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	public boolean isDefaultProjectUpdated() {
		return defaultProjectUpdated;
	}

	public void setDefaultProjectUpdated(boolean defaultProjectUpdated) {
		this.defaultProjectUpdated = defaultProjectUpdated;
	}

	@XmlTransient
	@Transient
	@Override
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
