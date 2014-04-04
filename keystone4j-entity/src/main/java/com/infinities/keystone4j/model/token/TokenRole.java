package com.infinities.keystone4j.model.token;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.assignment.Role;

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
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
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
