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
package com.infinities.keystone4j.model.identity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UpdateUserParam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	private final User user = new User();


	public String getName() {
		return user.getName();
	}

	public void setName(String name) {
		this.user.setName(name);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public void setPassword(String password) {
		user.setPassword(password);
	}

	public Boolean getEnabled() {
		return user.getEnabled();
	}

	public void setEnabled(Boolean enabled) {
		user.setEnabled(enabled);
	}

	public String getDefaultProjectId() {
		return user.getDefaultProjectId();
	}

	@XmlElement(name = "default_project_id")
	public void setDefaultProjectId(String defaultProjectId) {
		user.setDefaultProjectId(defaultProjectId);
	}

	public String getDescription() {
		return user.getDescription();
	}

	public void setDescription(String description) {
		this.user.setDescription(description);
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

}
