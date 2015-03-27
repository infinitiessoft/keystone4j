package com.infinities.keystone4j.model.identity.wrapper;

import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.model.identity.UpdateUserParam;
import com.infinities.keystone4j.model.identity.User;

public class UpdateUserParamWrapper {

	private UpdateUserParam user;


	public UpdateUserParamWrapper() {

	}

	public UpdateUserParamWrapper(UpdateUserParam user) {
		super();
		this.user = user;
	}

	public UpdateUserParam getUser() {
		return user;
	}

	public void setUser(UpdateUserParam user) {
		this.user = user;
	}

	@XmlTransient
	public User getRef() {
		User ref = new User();
		ref.setDefaultProjectId(user.getDefaultProjectId());
		ref.setDescription(user.getDescription());
		ref.setEnabled(user.getEnabled());
		ref.setName(user.getName());
		ref.setPassword(user.getPassword());
		return ref;
	}

}
