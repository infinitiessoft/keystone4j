package com.infinities.keystone4j.model.identity.wrapper;

import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.model.identity.CreateUserParam;
import com.infinities.keystone4j.model.identity.User;

public class CreateUserParamWrapper {

	private CreateUserParam user;


	public CreateUserParamWrapper() {

	}

	public CreateUserParamWrapper(CreateUserParam user) {
		super();
		this.user = user;
	}

	public CreateUserParam getUser() {
		return user;
	}

	public void setUser(CreateUserParam user) {
		this.user = user;
	}

	@XmlTransient
	public User getRef() {
		return user.getUser();
	}

}
