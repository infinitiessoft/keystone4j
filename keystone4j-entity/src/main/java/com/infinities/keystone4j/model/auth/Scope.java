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

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.trust.Trust;

public class Scope {

	private Project project;
	private Domain domain;
	@XmlElement(name = "OS-TRUST:trust")
	private Trust trust;


	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

}
