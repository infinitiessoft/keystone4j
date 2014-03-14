package com.infinities.keystone4j.auth.model;

import com.infinities.keystone4j.identity.model.User;

public interface AuthData {

	User getUser();

	String getId();

}
