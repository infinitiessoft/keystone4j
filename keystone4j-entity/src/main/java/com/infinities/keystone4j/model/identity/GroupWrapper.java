package com.infinities.keystone4j.model.identity;

import com.infinities.keystone4j.model.MemberWrapper;

public class GroupWrapper implements MemberWrapper<Group> {

	private Group group;


	public GroupWrapper() {

	}

	public GroupWrapper(Group group) {
		this.group = group;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(group,
		// baseUrl);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public void setRef(Group ref) {
		this.group = ref;
	}
}
