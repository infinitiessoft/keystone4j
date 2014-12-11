package com.infinities.keystone4j.model.identity;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class GroupsWrapper implements CollectionWrapper<Group> {

	private List<Group> groups;
	private boolean truncated;

	private Links links = new Links();


	public GroupsWrapper() {

	}

	public GroupsWrapper(List<Group> groups) {
		this.groups = groups;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
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
	public void setRefs(List<Group> refs) {
		this.groups = refs;
	}

}
