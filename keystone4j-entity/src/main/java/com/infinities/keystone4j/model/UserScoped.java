package com.infinities.keystone4j.model;

import com.infinities.keystone4j.model.identity.User;

public interface UserScoped {

	User getUser();

	void setUser(User user);

	void setUserId(String userId);

	String getUserId();
}
