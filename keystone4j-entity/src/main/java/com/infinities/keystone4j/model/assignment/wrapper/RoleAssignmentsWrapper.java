package com.infinities.keystone4j.model.assignment.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment;
import com.infinities.keystone4j.model.common.CollectionLinks;

public class RoleAssignmentsWrapper implements CollectionWrapper<FormattedRoleAssignment> {

	private List<FormattedRoleAssignment> roleAssignments;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public RoleAssignmentsWrapper() {

	}

	public RoleAssignmentsWrapper(List<FormattedRoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
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
	public void setRefs(List<FormattedRoleAssignment> refs) {
		this.roleAssignments = refs;
	}

	@XmlElement(name = "role_assignments")
	public List<FormattedRoleAssignment> getRefs() {
		return roleAssignments;
	}

}
