package com.infinities.keystone4j.model.identity.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.identity.User;

public class UsersWrapper implements CollectionWrapper<User> {

	private List<User> users;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public UsersWrapper() {

	}

	public UsersWrapper(List<User> users) {
		this.users = users;
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
	public void setRefs(List<User> refs) {
		this.users = refs;
	}

	@XmlElement(name = "users")
	public List<User> getRefs() {
		return users;
	}

}
