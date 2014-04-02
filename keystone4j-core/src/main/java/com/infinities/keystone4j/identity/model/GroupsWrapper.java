package com.infinities.keystone4j.identity.model;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.common.model.Links;

public class GroupsWrapper {

	private List<Group> groups;
	private Links links = new Links();


	public GroupsWrapper(List<Group> groups, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/groups/";
		this.groups = groups;
		for (Group group : groups) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(group, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
