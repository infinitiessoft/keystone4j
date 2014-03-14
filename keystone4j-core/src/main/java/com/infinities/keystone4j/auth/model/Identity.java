package com.infinities.keystone4j.auth.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonAnyGetter;

import com.infinities.keystone4j.token.model.Token;

public class Identity {

	private List<String> methods = new ArrayList<String>(0);

	private final Map<String, AuthData> authMethods = new HashMap<String, AuthData>();


	// private Token token;

	// private User user;

	public Identity() {

	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	// public Password getPassword() {
	// return password;
	// }
	//
	// public void setPassword(Password password) {
	// this.password = password;
	// }
	//
	// public Token getToken() {
	// return token;
	// }
	//
	// public void setToken(Token token) {
	// this.token = token;
	// }

	@XmlTransient
	public Map<String, AuthData> getAuthMethods() {
		return authMethods;
	}

	@JsonAnyGetter
	public Map<String, AuthData> any() {
		return authMethods;
	}

	public void setPassword(Password password) {
		authMethods.put("password", password);
	}

	public void setToken(Token token) {
		authMethods.put("token", token);
	}

	// public User getUser() {
	// return user;
	// }
	//
	// public void setUser(User user) {
	// this.user = user;
	// }

}
