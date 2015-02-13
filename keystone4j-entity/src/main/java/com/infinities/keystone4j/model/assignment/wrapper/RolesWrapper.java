package com.infinities.keystone4j.model.assignment.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.utils.Views;

public class RolesWrapper implements CollectionWrapper<Role> {

	private List<Role> roles;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public RolesWrapper() {

	}

	public RolesWrapper(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
		this.links = links;
	}

	@JsonView(Views.All.class)
	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Role> refs) {
		this.roles = refs;
	}

	@XmlElement(name = "roles")
	public List<Role> getRefs() {
		return roles;
	}

}