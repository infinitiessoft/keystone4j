package com.infinities.keystone4j.model.auth;

import com.infinities.keystone4j.model.identity.User;

public interface AuthData {

	User getUser();

	String getId();

}
