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
