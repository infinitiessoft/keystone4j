package com.infinities.keystone4j.model.identity;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class UsersWrapper implements CollectionWrapper<User> {

	private List<User> users;
	private boolean truncated;

	private Links links = new Links();


	public UsersWrapper() {

	}

	public UsersWrapper(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
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
	public void setRefs(List<User> refs) {
		this.users = refs;
	}

}
