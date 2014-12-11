package com.infinities.keystone4j.model.identity;

import com.infinities.keystone4j.model.MemberWrapper;

public class UserWrapper implements MemberWrapper<User> {

	private User user;


	public UserWrapper() {

	}

	public UserWrapper(User user) {
		this.user = user;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(user,
		// baseUrl);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void setRef(User ref) {
		this.user = ref;
	}
}
