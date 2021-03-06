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
package com.infinities.keystone4j.model.token.v2;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.utils.jackson.adapter.ExpireDateAdapter;
import com.infinities.keystone4j.model.utils.jackson.adapter.IssueDateAdapter;

public class TokenV2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	@XmlJavaTypeAdapter(IssueDateAdapter.class)
	private Calendar issued_at;
	@XmlJavaTypeAdapter(ExpireDateAdapter.class)
	private Calendar expires;

	private Project tenant;

	private Bind bind;

	@XmlTransient
	private List<String> auditIds;
	@XmlTransient
	private Trust trust;


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

	public Project getTenant() {
		return tenant;
	}

	public void setTenant(Project tenant) {
		this.tenant = tenant;
	}

	public Bind getBind() {
		return bind;
	}

	public void setBind(Bind bind) {
		this.bind = bind;
	}

	@XmlTransient
	public List<String> getAuditIds() {
		return auditIds;
	}

	@XmlTransient
	public void setAuditIds(List<String> auditIds) {
		this.auditIds = auditIds;
	}

	@XmlTransient
	public Trust getTrust() {
		return trust;
	}

	@XmlTransient
	public void setTrust(Trust trust) {
		this.trust = trust;
	}


	public static class Trust {

		private String id;
		@XmlElement(name = "trustor_id")
		private String trustorId;
		@XmlElement(name = "trustee_id")
		private String trusteeId;


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTrustorId() {
			return trustorId;
		}

		public void setTrustorId(String trustorId) {
			this.trustorId = trustorId;
		}

		public String getTrusteeId() {
			return trusteeId;
		}

		public void setTrusteeId(String trusteeId) {
			this.trusteeId = trusteeId;
		}

	}

	// @Override
	// public String toString() {
	// return "Token [id=" + id + ", issued_at=" + issued_at + ", expires=" +
	// expires + ", tenant=" + tenant + "]";
	// }

}
