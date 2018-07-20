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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinities.keystone4j.middleware.model.adapter.ExpireDateAdapter;
import com.infinities.keystone4j.middleware.model.adapter.IssueDateAdapter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	@XmlJavaTypeAdapter(IssueDateAdapter.class)
	private Calendar issued_at;
	@XmlJavaTypeAdapter(ExpireDateAdapter.class)
	private Calendar expires;

	private Tenant tenant;

	private Bind bind;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(Calendar issued_at) {
		this.issued_at = issued_at;
	}

	public Calendar getExpires() {
		return expires;
	}

	public void setExpires(Calendar expires) {
		this.expires = expires;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Bind getBind() {
		return bind;
	}

	public void setBind(Bind bind) {
		this.bind = bind;
	}

	// @Override
	// public String toString() {
	// return "Token [id=" + id + ", issued_at=" + issued_at + ", expires=" +
	// expires + ", tenant=" + tenant + "]";
	// }

}
