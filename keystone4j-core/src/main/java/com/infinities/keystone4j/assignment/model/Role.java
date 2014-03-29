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
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.model.TokenRole;
import com.infinities.keystone4j.trust.model.TrustRole;

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
	private Set<TrustRole> trustRoles = new HashSet<TrustRole>(0);
	private Set<TokenRole> tokenRoles = new HashSet<TokenRole>(0);
	// private Set<Assignment> assignments = new HashSet<Assignment>(0);
	private Set<GroupProjectGrantMetadata> groupProjectMetadatas = new HashSet<GroupProjectGrantMetadata>(0);
	private Set<GroupDomainGrantMetadata> groupDomainMetadatas = new HashSet<GroupDomainGrantMetadata>(0);
	private Set<UserProjectGrantMetadata> userProjectMetadatas = new HashSet<UserProjectGrantMetadata>(0);
	private Set<UserDomainGrantMetadata> userDomainMetadatas = new HashSet<UserDomainGrantMetadata>(0);

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
	public Set<GroupProjectGrantMetadata> getGroupProjectMetadatas() {
		return groupProjectMetadatas;
	}

	@JsonView(Views.All.class)
	public void setGroupProjectMetadatas(Set<GroupProjectGrantMetadata> groupProjectMetadatas) {
		this.groupProjectMetadatas = groupProjectMetadatas;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<GroupDomainGrantMetadata> getGroupDomainMetadatas() {
		return groupDomainMetadatas;
	}

	@JsonView(Views.All.class)
	public void setGroupDomainMetadatas(Set<GroupDomainGrantMetadata> groupDomainMetadatas) {
		this.groupDomainMetadatas = groupDomainMetadatas;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserProjectGrantMetadata> getUserProjectMetadatas() {
		return userProjectMetadatas;
	}

	@JsonView(Views.All.class)
	public void setUserProjectMetadatas(Set<UserProjectGrantMetadata> userProjectMetadatas) {
		this.userProjectMetadatas = userProjectMetadatas;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserDomainGrantMetadata> getUserDomainMetadatas() {
		return userDomainMetadatas;
	}

	@JsonView(Views.All.class)
	public void setUserDomainMetadatas(Set<UserDomainGrantMetadata> userDomainMetadatas) {
		this.userDomainMetadatas = userDomainMetadatas;
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
