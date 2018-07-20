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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CreateUserParam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	private final User user = new User();


	@XmlElement(name = "domain_id")
	public String getDomainId() {
		return user.getDomainId();
	}

	public void setDomainId(String domainId) {
		this.user.setDomainId(domainId);
	}

	@NotNull(message = "name field is required and cannot be empty")
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

	@XmlElement(name = "default_project_id")
	public String getDefaultProjectId() {
		return user.getDefaultProjectId();
	}

	public void setDefaultProjectId(String defaultProjectId) {
		user.setDefaultProjectId(defaultProjectId);
	}

	public String getDescription() {
		return user.getDescription();
	}

	public void setDescription(String description) {
		this.user.setDescription(description);
	}

	@XmlElement(name = "firstname")
	public String getFirstName() {
		return user.getFirstName();
	}

	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	@XmlElement(name = "lastname")
	public String getLastName() {
		return user.getLastName();
	}

	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

}
