package com.infinities.keystone4j.model.token;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

@Entity
@Table(name = "TOKEN_METADATA", schema = "PUBLIC", catalog = "PUBLIC")
public class Metadata implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Token token;
	private String trustId;
	private String trusteeUserId;
	private Set<String> roles;


	public Metadata() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "metadata")
	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Column(name = "TRUST_ID", length = 100)
	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	@ElementCollection
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Column(name = "TRUSTEE_USER_ID", length = 100)
	@XmlElement(name = "trustee_user_id")
	public String getTrusteeUserId() {
		return trusteeUserId;
	}

	public void setTrusteeUserId(String trusteeUserId) {
		this.trusteeUserId = trusteeUserId;
	}

}
