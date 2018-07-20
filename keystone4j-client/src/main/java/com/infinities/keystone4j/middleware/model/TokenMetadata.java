/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlElement;

public class TokenMetadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Calendar expires;
	@XmlElement(name = "user_id")
	private String userid;
	@XmlElement(name = "tenant_id")
	private String tenantid;
	@XmlElement(name = "domain_id")
	private String domainid;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getExpires() {
		return expires;
	}

	public void setExpires(Calendar expires) {
		this.expires = expires;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public void setDomainId(String domainid) {
		this.domainid = domainid;
	}

	public String getDomain() {
		return domainid;
	}

}
