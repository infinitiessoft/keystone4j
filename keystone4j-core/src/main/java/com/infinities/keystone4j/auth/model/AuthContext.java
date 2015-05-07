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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.token.Bind;

@Deprecated
public class AuthContext {

	@XmlElement(name = "method_names")
	private List<String> methodNames;
	@XmlElement(name = "user_id")
	private String userid;
	@XmlElement(name = "project_id")
	private String projectid;
	@XmlElement(name = "access_token_id")
	private String accessTokenid;
	@XmlElement(name = "expires_at")
	private Date expiresAt;
	@XmlElement(name = "extra")
	private String extra;

	private Bind bind = new Bind();
	private String domainid;
	private Set<Role> roles = new HashSet<Role>(0);


	// private Environment environment;

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public String getAccessTokenid() {
		return accessTokenid;
	}

	public void setAccessTokenid(String accessTokenid) {
		this.accessTokenid = accessTokenid;
	}

	public AuthContext() {
		methodNames = Lists.newArrayList();
	}

	public List<String> getMethodNames() {
		return methodNames;
	}

	public void setMethodNames(List<String> methodNames) {
		this.methodNames = methodNames;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Bind getBind() {
		return bind;
	}

	public void setBind(Bind bind) {
		this.bind = bind;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	// public Environment getEnvironment() {
	// return environment;
	// }
	//
	// public void setEnvironment(Environment environment) {
	// this.environment = environment;
	// }

}
