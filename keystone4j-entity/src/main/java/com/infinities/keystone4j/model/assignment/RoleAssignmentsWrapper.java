package com.infinities.keystone4j.model.assignment;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

@XmlRootElement(name = "role_assignments")
public class RoleAssignmentsWrapper implements CollectionWrapper<RoleAssignment> {

	private List<RoleAssignment> roleAssignments;
	private boolean truncated;

	private Links links = new Links();


	public RoleAssignmentsWrapper() {

	}

	public RoleAssignmentsWrapper(List<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}

	public List<RoleAssignment> getRoleAssignments() {
		return roleAssignments;
	}

	public void setRoleAssignments(List<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}

	@Override
	public Links getLinks() {
		return links;
	}

	@Override
	public void setLinks(Links links) {
		this.links = links;
	}

	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<RoleAssignment> refs) {
		this.roleAssignments = refs;
	}

}
