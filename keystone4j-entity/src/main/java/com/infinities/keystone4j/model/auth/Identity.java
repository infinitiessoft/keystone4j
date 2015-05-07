/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
