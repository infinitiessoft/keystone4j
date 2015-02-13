package com.infinities.keystone4j.model.identity.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.identity.Group;

public class GroupsWrapper implements CollectionWrapper<Group> {

	private List<Group> groups;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public GroupsWrapper() {

	}

	public GroupsWrapper(List<Group> groups) {
		this.groups = groups;
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
	public void setRefs(List<Group> refs) {
		this.groups = refs;
	}

	@XmlElement(name = "groups")
	public List<Group> getRefs() {
		return groups;
	}

}
