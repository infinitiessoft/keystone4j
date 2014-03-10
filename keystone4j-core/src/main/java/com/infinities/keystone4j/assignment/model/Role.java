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
import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.BaseEntity;
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

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		setExtraUpdated(true);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<TrustRole> getTrustRoles() {
		return trustRoles;
	}

	public void setTrustRoles(Set<TrustRole> trustRoles) {
		this.trustRoles = trustRoles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<GroupProjectGrantMetadata> getGroupProjectMetadatas() {
		return groupProjectMetadatas;
	}

	public void setGroupProjectMetadatas(Set<GroupProjectGrantMetadata> groupProjectMetadatas) {
		this.groupProjectMetadatas = groupProjectMetadatas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<GroupDomainGrantMetadata> getGroupDomainMetadatas() {
		return groupDomainMetadatas;
	}

	public void setGroupDomainMetadatas(Set<GroupDomainGrantMetadata> groupDomainMetadatas) {
		this.groupDomainMetadatas = groupDomainMetadatas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserProjectGrantMetadata> getUserProjectMetadatas() {
		return userProjectMetadatas;
	}

	public void setUserProjectMetadatas(Set<UserProjectGrantMetadata> userProjectMetadatas) {
		this.userProjectMetadatas = userProjectMetadatas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<UserDomainGrantMetadata> getUserDomainMetadatas() {
		return userDomainMetadatas;
	}

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

	@Column(name = "INHERITEDTO", nullable = false)
	public String getInheritedTo() {
		return inheritedTo;
	}

	public void setInheritedTo(String inheritedTo) {
		this.inheritedTo = inheritedTo;
	}

	public boolean isNameUpdated() {
		return nameUpdated;
	}

	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.ALL)
	public Set<TokenRole> getTokenRoles() {
		return tokenRoles;
	}

	public void setTokenRoles(Set<TokenRole> tokenRoles) {
		this.tokenRoles = tokenRoles;
	}

	@XmlTransient
	@Override
	public User getUser() {
		throw new IllegalStateException("propert 'user' not exist");
	}

	@XmlTransient
	@Override
	public Domain getDomain() {
		throw new IllegalStateException("propert 'domain' not exist");
	}

	@XmlTransient
	@Override
	public Project getProject() {
		throw new IllegalStateException("propert 'project' not exist");
	}

}
