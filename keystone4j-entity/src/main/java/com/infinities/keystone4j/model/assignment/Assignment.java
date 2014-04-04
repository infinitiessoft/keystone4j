package com.infinities.keystone4j.model.assignment;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

//@Entity
//@Table(name = "ASSIGNMENT", schema = "PUBLIC", catalog = "PUBLIC")
public class Assignment extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8976543300305304271L;
	private Role role;
	private User user;
	private Project project;
	private Group group;
	private Domain domain;
	private Boolean inheritedToProjects;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMAINID", nullable = false)
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLEID", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "INHERITEDTO", nullable = false)
	public Boolean getInheritedToProjects() {
		return inheritedToProjects;
	}

	public void setInheritedToProjects(Boolean inheritedToProjects) {
		this.inheritedToProjects = inheritedToProjects;
	}

}
