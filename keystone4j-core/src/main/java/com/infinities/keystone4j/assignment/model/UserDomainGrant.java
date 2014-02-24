package com.infinities.keystone4j.assignment.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.identity.model.User;

@Entity
@Table(name = "USER_DOMAIN_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"USERID", "DOMAINID" }) })
public class UserDomainGrant extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private User user;
	private Domain domain;
	private Set<UserDomainGrantMetadata> metadatas = new HashSet<UserDomainGrantMetadata>(0);


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grant", cascade = CascadeType.ALL)
	public Set<UserDomainGrantMetadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(Set<UserDomainGrantMetadata> metadatas) {
		this.metadatas = metadatas;
	}

}
