package com.infinities.keystone4j.assignment.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.Token;

@Entity
@Table(name = "DOMAIN", schema = "PUBLIC", catalog = "PUBLIC")
public class Domain extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4125628352361369176L;

	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private Boolean enabled = true;
	private String extra;
	private Set<Project> projects = new HashSet<Project>(0);
	private Set<Group> groups = new HashSet<Group>(0);
	private Set<User> users = new HashSet<User>(0);
	private Set<Token> tokens = new HashSet<Token>(0);
	private Set<UserDomainGrant> userDomainGrants = new HashSet<UserDomainGrant>(0);
	private Set<GroupDomainGrant> groupDomainGrants = new HashSet<GroupDomainGrant>(0);
	// private Set<Assignment> assignments = new HashSet<Assignment>(0);
	private boolean nameUpdated = false;
	private boolean enabledUpdated = false;
	private boolean extraUpdated = false;


	@Column(name = "NAME", length = 64, nullable = false, unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<UserDomainGrant> getUserDomainGrants() {
		return userDomainGrants;
	}

	public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
		this.userDomainGrants = userDomainGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<GroupDomainGrant> getGroupDomainGrants() {
		return groupDomainGrants;
	}

	public void setGroupDomainGrants(Set<GroupDomainGrant> groupDomainGrants) {
		this.groupDomainGrants = groupDomainGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade = CascadeType.ALL)
	public Set<Token> getTokens() {
		return tokens;
	}

	public void setTokens(Set<Token> tokens) {
		this.tokens = tokens;
	}

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "domain", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }

	public boolean isNameUpdated() {
		return nameUpdated;
	}

	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
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

	@Override
	public User getUser() {
		throw new IllegalStateException("propert 'user' not exist");
	}

	@Override
	public Domain getDomain() {
		throw new IllegalStateException("propert 'domain' not exist");
	}

	@Override
	public Project getProject() {
		throw new IllegalStateException("propert 'project' not exist");
	}

}
