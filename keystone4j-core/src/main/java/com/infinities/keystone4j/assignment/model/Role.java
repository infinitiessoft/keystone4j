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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.TokenRole;
import com.infinities.keystone4j.trust.model.TrustRole;
import com.infinities.keystone4j.utils.jackson.Views;

@Entity
@Table(name = "ROLE", schema = "PUBLIC", catalog = "PUBLIC")
public class Role extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1497968571650652616L;
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private String extra;

	// private Set<Assignment> assignments = new HashSet<Assignment>(0);
	private Set<GroupProjectGrant> groupProjectGrants = new HashSet<GroupProjectGrant>(0);
	private Set<GroupDomainGrant> groupDomainGrants = new HashSet<GroupDomainGrant>(0);
	private Set<UserProjectGrant> userProjectGrants = new HashSet<UserProjectGrant>(0);
	private Set<UserDomainGrant> userDomainGrants = new HashSet<UserDomainGrant>(0);
	private Set<TrustRole> trustRoles = new HashSet<TrustRole>(0);
	private Set<TokenRole> tokenRoles = new HashSet<TokenRole>(0);

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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<TrustRole> getTrustRoles() {
		return trustRoles;
	}

	@JsonView(Views.All.class)
	public void setTrustRoles(Set<TrustRole> trustRoles) {
		this.trustRoles = trustRoles;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<GroupProjectGrant> getGroupProjectGrants() {
		return groupProjectGrants;
	}

	@JsonView(Views.All.class)
	public void setGroupProjectGrants(Set<GroupProjectGrant> groupProjectGrants) {
		this.groupProjectGrants = groupProjectGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<GroupDomainGrant> getGroupDomainGrants() {
		return groupDomainGrants;
	}

	@JsonView(Views.All.class)
	public void setGroupDomainGrants(Set<GroupDomainGrant> groupDomainGrants) {
		this.groupDomainGrants = groupDomainGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserProjectGrant> getUserProjectGrants() {
		return userProjectGrants;
	}

	@JsonView(Views.All.class)
	public void setUserProjectGrants(Set<UserProjectGrant> userProjectGrants) {
		this.userProjectGrants = userProjectGrants;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserDomainGrant> getUserDomainGrants() {
		return userDomainGrants;
	}

	@JsonView(Views.All.class)
	public void setUserDomainGrants(Set<UserDomainGrant> userDomainGrants) {
		this.userDomainGrants = userDomainGrants;
	}

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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<TokenRole> getTokenRoles() {
		return tokenRoles;
	}

	@JsonView(Views.All.class)
	public void setTokenRoles(Set<TokenRole> tokenRoles) {
		this.tokenRoles = tokenRoles;
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
	public Domain getDomain() {
		throw new IllegalStateException("propert 'domain' not exist");
	}

	@XmlTransient
	@Transient
	@Override
	@JsonIgnore
	public Project getProject() {
		throw new IllegalStateException("propert 'project' not exist");
	}

}
