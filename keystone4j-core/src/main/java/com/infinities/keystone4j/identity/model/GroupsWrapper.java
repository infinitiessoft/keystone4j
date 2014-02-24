package com.infinities.keystone4j.identity.model;

import java.util.List;

public class GroupsWrapper {

	private List<Group> groups;


	public GroupsWrapper(List<Group> groups) {
		super();
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
