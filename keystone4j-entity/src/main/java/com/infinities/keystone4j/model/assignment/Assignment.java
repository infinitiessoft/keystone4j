package com.infinities.keystone4j.model.assignment;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.BaseEntity;

public class Assignment extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8976543300305304271L;
	@XmlElement(name = "role_id")
	private String roleId;
	@XmlElement(name = "user_id")
	private String userId;
	@XmlElement(name = "project_id")
	private String projectId;
	@XmlElement(name = "group_id")
	private String groupId;
	@XmlElement(name = "domain_id")
	private String domainId;
	@XmlElement(name = "inherited_to_projects")
	private String inheritedToProjects;


	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getInheritedToProjects() {
		return inheritedToProjects;
	}

	public void setInheritedToProjects(String inheritedToProjects) {
		this.inheritedToProjects = inheritedToProjects;
	}

}
