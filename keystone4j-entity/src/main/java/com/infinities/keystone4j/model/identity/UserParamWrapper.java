package com.infinities.keystone4j.model.identity;

public class UserParamWrapper {

	private UserParam user;


	public UserParamWrapper() {

	}

	public UserParamWrapper(UserParam user) {
		this.user = user;
	}

	public UserParam getUser() {
		return user;
	}

	public void setUser(UserParam user) {
		this.user = user;
	}
}
