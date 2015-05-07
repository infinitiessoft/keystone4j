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
package com.infinities.keystone4j.auth.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4935169630065011911L;
	private List<String> methods;
	private Map<String, Object> extras;
	@XmlElement(name = "user_id")
	private String userid;
	private final Map<String, Exception> authResponse;


	public AuthResponse() {
		methods = Lists.newArrayList();
		extras = Maps.newHashMap();
		authResponse = Maps.newHashMap();
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public void set(String key, Exception value) {
		authResponse.put(key, value);
	}

	public Exception get(String key) {
		return authResponse.get(key);
	}

	@Override
	public String toString() {
		return "AuthResponse [methods=" + methods + ", extras=" + extras + ", userid=" + userid + "]";
	}

}
