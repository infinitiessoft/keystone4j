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

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.policy.model.PolicyEntity;

@Entity
@Table(name = "GROUP", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"DOMAINID", "NAME" }) })
public class Group extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1508551185229141627L;
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private Domain domain;
	private String extra;
	private Set<UserGroupMembership> userGroupMemberships = new HashSet<UserGroupMembership>(0);
	private Set<GroupProjectGrant> groupProjectGrants = new HashSet<GroupProjectGrant>(0);
	private Set<GroupDomainGrant> groupDomainGrants = new HashSet<GroupDomainGrant>(0);
	private boolean nameUpdated = false;
	private boolean domainUpdated = false;
	private boolean extraUpdated = false;


	// private Set<Assignment> assignments = new HashSet<Assignment>(0);

	@Column(name = "NAME", length = 255, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setNameUpdated(true);
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

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		setExtraUpdated(true);
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
	public Set<UserGroupMembership> getUserGroupMemberships() {
		return userGroupMemberships;
	}

	public void setUserGroupMemberships(Set<UserGroupMembership> userGroupMemberships) {
		this.userGroupMemberships = userGroupMemberships;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
	public Set<GroupProjectGrant> getGroupProjectGrants() {
		return groupProjectGrants;
	}

	public void setGroupProjectGrants(Set<GroupProjectGrant> groupProjectGrants) {
		this.groupProjectGrants = groupProjectGrants;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
	public Set<GroupDomainGrant> getGroupDomainGrants() {
		return groupDomainGrants;
	}

	public void setGroupDomainGrants(Set<GroupDomainGrant> groupDomainGrants) {
		this.groupDomainGrants = groupDomainGrants;
	}

	public boolean isNameUpdated() {
		return nameUpdated;
	}

	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	public boolean isDomainUpdated() {
		return domainUpdated;
	}

	public void setDomainUpdated(boolean domainUpdated) {
		this.domainUpdated = domainUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlTransient
	@Transient
	@Override
	public User getUser() {
		throw new IllegalStateException("propert 'user' not exist");
	}

	@XmlTransient
	@Transient
	@Override
	public Project getProject() {
		throw new IllegalStateException("propert 'project' not exist");
	}

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade =
	// CascadeType.ALL)
	// public Set<Assignment> getAssignments() {
	// return assignments;
	// }
	//
	// public void setAssignments(Set<Assignment> assignments) {
	// this.assignments = assignments;
	// }

}
