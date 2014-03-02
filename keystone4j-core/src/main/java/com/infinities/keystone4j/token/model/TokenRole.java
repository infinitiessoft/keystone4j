package com.infinities.keystone4j.token.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Role;

@Entity
@Table(name = "TOKEN_ROLE", schema = "PUBLIC", catalog = "PUBLIC")
public class TokenRole extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7707735497658817097L;
	private Token token;
	private Role role;


	public TokenRole() {

	}

	public TokenRole(Token token, Role role) {
		this.token = token;
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOKENID", nullable = false)
	public Token getTrust() {
		return token;
	}

	public void setTrust(Token trust) {
		this.token = trust;
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
