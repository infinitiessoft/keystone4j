package com.infinities.keystone4j.model.identity.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;

public class UserWrapper implements MemberWrapper<User> {

	private User user;


	public UserWrapper() {

	}

	public UserWrapper(User user) {
		this.user = user;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(user,
		// baseUrl);
	}

	@Override
	public void setRef(User ref) {
		this.user = ref;
	}

	@XmlElement(name = "user")
	@Override
	public User getRef() {
		return user;
	}
}
