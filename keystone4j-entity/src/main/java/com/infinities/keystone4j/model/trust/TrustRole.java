package com.infinities.keystone4j.model.trust;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.assignment.Role;

@Entity
@Table(name = "TRUST_ROLE", schema = "PUBLIC", catalog = "PUBLIC")
public class TrustRole extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7707735497658817097L;
	private Trust trust;
	private Role role;


	public TrustRole() {

	}

	public TrustRole(Trust trust, Role role) {
		this.trust = trust;
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRUSTID", nullable = false)
	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLEID", nullable = false)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
