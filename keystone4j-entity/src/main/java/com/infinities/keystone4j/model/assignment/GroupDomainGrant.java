/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
//package com.infinities.keystone4j.model.assignment;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
//
//import com.infinities.keystone4j.model.BaseEntity;
//import com.infinities.keystone4j.model.identity.Group;
//
//@Entity
//@Table(name = "GROUP_DOMAIN_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
//		"GROUPID", "DOMAINID", "ROLEID" }) })
//public class GroupDomainGrant extends BaseEntity implements java.io.Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5727136446409993382L;
//	private Group group;
//	private Domain domain;
//	private Role role;
//
//
//	// private Set<GroupDomainGrantMetadata> groupDomainGrantMetadatas = new
//	// HashSet<GroupDomainGrantMetadata>(0);
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "DOMAINID", nullable = false)
//	public Domain getDomain() {
//		return domain;
//	}
//
//	public void setDomain(Domain domain) {
//		this.domain = domain;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "GROUPID", nullable = false)
//	public Group getGroup() {
//		return group;
//	}
//
//	public void setGroup(Group group) {
//		this.group = group;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ROLEID", nullable = false)
//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
//	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "grant", cascade =
//	// CascadeType.ALL)
//	// public Set<GroupDomainGrantMetadata> getGroupDomainGrantMetadatas() {
//	// return groupDomainGrantMetadatas;
//	// }
//	//
//	// public void setGroupDomainGrantMetadatas(Set<GroupDomainGrantMetadata>
//	// groupDomainGrantMetadatas) {
//	// this.groupDomainGrantMetadatas = groupDomainGrantMetadatas;
//	// }
//
// }
