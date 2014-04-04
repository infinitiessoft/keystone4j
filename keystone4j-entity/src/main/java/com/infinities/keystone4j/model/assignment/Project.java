package com.infinities.keystone4j.model.assignment;

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
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.endpointfilter.ProjectEndpoint;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "PROJECT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DOMAINID", "NAME" }) })
public class Project extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9123073137242329603L;
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private Domain domain;
	private Boolean enabled = true;
	private String extra;
	private Set<UserProjectGrant> userProjectGrants = new HashSet<UserProjectGrant>(0);
	private Set<GroupProjectGrant> groupProjectGrants = new HashSet<GroupProjectGrant>(0);
	private Set<Credential> credentials = new HashSet<Credential>(0);
	private Set<Token> tokens = new HashSet<Token>(0);
	private Set<User> users = new HashSet<User>(0);
	private Set<Trust> trusts = new HashSet<Trust>(0);
	private Set<ProjectEndpoint> projectEndpoints = new HashSet<ProjectEndpoint>(0);
	// private Set<Assignment> assignments = new HashSet<Assignment>(0);
	private boolean nameUpdated = false;
	private boolean domainUpdated = false;
	private boolean enabledUpdated = false;
	private boolean extraUpdated = false;


	@Column(name = "NAME", length = 64, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setNameUpdate(true);
	}

	@XmlTransient
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	@XmlTransient
	public void setDomain(Domain domain) {
		this.domain = domain;
		setDomainUpdated(true);
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
		if (!(domainid == null || domainid.length() == 0)) {
			Domain domain = new Domain();
			domain.setId(domainid);
			setDomain(domain);
		}
	}

	@Column(name = "ENABLED", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		setEnabledUpdate(true);
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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<UserProjectGrant> getUserProjectGrants() {
		return userProjectGrants;
	}

	@JsonView(Views.All.class)
	public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants) {
		this.userProjectGrants = userProjectGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<GroupProjectGrant> getGroupProjectGrants() {
		return groupProjectGrants;
	}

	@JsonView(Views.All.class)
	public void setGroupProjectGrants(Set<GroupProjectGrant> groupProjectGrants) {
		this.groupProjectGrants = groupProjectGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<Credential> getCredentials() {
		return credentials;
	}

	@JsonView(Views.All.class)
	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	@JsonView(Views.All.class)
	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "default_project", cascade = CascadeType.ALL)
	public Set<User> getUsers() {
		return users;
	}

	@JsonView(Views.All.class)
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }

	@Transient
	@XmlTransient
	public boolean isNameUpdate() {
		return nameUpdated;
	}

	@Transient
	@XmlTransient
	public void setNameUpdate(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	@Transient
	@XmlTransient
	public boolean isEnabledUpdate() {
		return enabledUpdated;
	}

	@Transient
	@XmlTransient
	public void setEnabledUpdate(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
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

	@Transient
	@XmlTransient
	public boolean isDomainUpdated() {
		return domainUpdated;
	}

	@Transient
	@XmlTransient
	public void setDomainUpdated(boolean domainUpdated) {
		this.domainUpdated = domainUpdated;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoint", cascade = CascadeType.ALL)
	public Set<ProjectEndpoint> getProjectEndpoints() {
		return projectEndpoints;
	}

	@JsonView(Views.All.class)
	public void setProjectEndpoints(Set<ProjectEndpoint> projectEndpoints) {
		this.projectEndpoints = projectEndpoints;
	}

	@XmlTransient
	@Transient
	@Override
	@JsonIgnore
	public User getUser() {
		throw new IllegalStateException("propert 'user' not exist");
	}

	@XmlTransient
	@Transient
	@Override
	@JsonIgnore
	public Project getProject() {
		throw new IllegalStateException("propert 'project' not exist");
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<Trust> getTrusts() {
		return trusts;
	}

	@JsonView(Views.All.class)
	public void setTrusts(Set<Trust> trusts) {
		this.trusts = trusts;
	}

}
