package com.infinities.keystone4j.model.identity;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class GroupWrapper {

	private Group group;


	public GroupWrapper() {

	}

	public GroupWrapper(Group group, ContainerRequestContext context) {
		this(group, context.getUriInfo().getBaseUri().toASCIIString() + "v3/groups/");
	}

	public GroupWrapper(Group group, String baseUrl) {
		super();
		this.group = group;
		ReferentialLinkUtils.instance.addSelfReferentialLink(group, baseUrl);
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
