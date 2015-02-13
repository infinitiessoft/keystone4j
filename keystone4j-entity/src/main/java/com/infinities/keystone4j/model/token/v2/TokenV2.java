package com.infinities.keystone4j.model.token.v2;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.token.Bind;

public class TokenV2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Calendar issued_at;

	private Calendar expires;

	private Project tenant;

	private Bind bind;

	private List<String> auditIds;

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

	public List<String> getAuditIds() {
		return auditIds;
	}

	public void setAuditIds(List<String> auditIds) {
		this.auditIds = auditIds;
	}

	public Trust getTrust() {
		return trust;
	}

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
