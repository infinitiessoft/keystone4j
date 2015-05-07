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
package com.infinities.keystone4j;

import javax.xml.bind.annotation.XmlElement;

public class KeystoneContext {

	public final static String CONTEXT_NAME = "openstack.context";

	private boolean isAdmin = false;
	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "subject_token_id")
	private String subjectTokenid;
	private Environment environment = new Environment();


	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public String getSubjectTokenid() {
		return subjectTokenid;
	}

	public void setSubjectTokenid(String subjectTokenid) {
		this.subjectTokenid = subjectTokenid;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
