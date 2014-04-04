package com.infinities.keystone4j.model.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.infinities.keystone4j.model.token.Token;

public class Identity {

	private List<String> methods = new ArrayList<String>(0);

	private final Map<String, AuthData> authMethods = new HashMap<String, AuthData>();


	public Identity() {

	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

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

}
