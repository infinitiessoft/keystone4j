package com.infinities.keystone4j.auth.model;

import java.util.List;

import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.model.Token;

public class Identity {

	private List<String> methods;
	private Password password;
	private Token token;
	private User user;


	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
