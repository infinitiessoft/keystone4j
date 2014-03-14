package com.infinities.keystone4j.assignment.model;

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

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.endpointfilter.model.ProjectEndpoint;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

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

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
		setDomainUpdated(true);
	}

	@Column(name = "ENABLED", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		setEnabledUpdate(true);
	}

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		setExtraUpdated(true);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<UserProjectGrant> getUserProjectGrants() {
		return userProjectGrants;
	}

	public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants) {
		this.userProjectGrants = userProjectGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<GroupProjectGrant> getGroupProjectGrants() {
		return groupProjectGrants;
	}

	public void setGroupProjectGrants(Set<GroupProjectGrant> groupProjectGrants) {
		this.groupProjectGrants = groupProjectGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
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

	public boolean isNameUpdate() {
		return nameUpdated;
	}

	public void setNameUpdate(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	public boolean isEnabledUpdate() {
		return enabledUpdated;
	}

	public void setEnabledUpdate(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	public boolean isDomainUpdated() {
		return domainUpdated;
	}

	public void setDomainUpdated(boolean domainUpdated) {
		this.domainUpdated = domainUpdated;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoint", cascade = CascadeType.ALL)
	public Set<ProjectEndpoint> getProjectEndpoints() {
		return projectEndpoints;
	}

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

}
