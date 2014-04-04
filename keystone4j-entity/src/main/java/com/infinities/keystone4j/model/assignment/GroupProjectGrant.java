package com.infinities.keystone4j.model.assignment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.identity.Group;

@Entity
@Table(name = "GROUP_PROJECT_GRANT", schema = "PUBLIC", catalog = "PUBLIC", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"GROUPID", "PROJECTID", "ROLEID" }) })
public class GroupProjectGrant extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5727136446409993382L;
	private Group group;
	private Project project;
	private Role role;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GROUPID", nullable = false)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLEID", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
