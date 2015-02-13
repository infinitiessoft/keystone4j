package com.infinities.keystone4j.model.identity.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;

public class GroupWrapper implements MemberWrapper<Group> {

	private Group group;


	public GroupWrapper() {

	}

	public GroupWrapper(Group group) {
		this.group = group;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(group,
		// baseUrl);
	}

	@Override
	public void setRef(Group ref) {
		this.group = ref;
	}

	@XmlElement(name = "group")
	@Override
	public Group getRef() {
		return group;
	}
}
