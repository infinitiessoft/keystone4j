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
import com.infinities.keystone4j.identity.model.Group;

@Entity
@Table(name = "GROUP_DOMAIN_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"GROUPID", "DOMAINID" }) })
public class GroupDomainGrant extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private Group group;
	private Domain domain;
	private Set<GroupDomainGrantMetadata> metadatas = new HashSet<GroupDomainGrantMetadata>(0);


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grant", cascade = CascadeType.ALL)
	public Set<GroupDomainGrantMetadata> getMetadatas() {
		return metadatas;
	}

	public void setMetadatas(Set<GroupDomainGrantMetadata> metadatas) {
		this.metadatas = metadatas;
	}

}
