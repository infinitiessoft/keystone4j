package com.infinities.keystone4j.model.assignment;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class RolesWrapper implements CollectionWrapper<Role> {

	private List<Role> roles;
	private boolean truncated;

	private Links links = new Links();


	public RolesWrapper() {

	}

	public RolesWrapper(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
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
	public void setRefs(List<Role> refs) {
		this.roles = refs;
	}

}