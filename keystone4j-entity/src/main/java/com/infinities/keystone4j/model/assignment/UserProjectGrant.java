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
//import com.infinities.keystone4j.model.identity.User;
//
//@Entity
//@Table(name = "USER_PROJECT_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
//		"USERID", "PROJECTID", "ROLEID" }) })
//public class UserProjectGrant extends BaseEntity implements java.io.Serializable {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 5727136446409993382L;
//	private User user;
//	private Project project;
//	private Role role;
//
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "USERID", nullable = false)
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "PROJECTID", nullable = false)
//	public Project getProject() {
//		return project;
//	}
//
//	public void setProject(Project project) {
//		this.project = project;
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
// }
