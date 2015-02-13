package com.infinities.keystone4j.model.token;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.common.Link;

@Entity
@Table(name = "TOKEN_METADATA", schema = "PUBLIC", catalog = "PUBLIC")
public class Metadata implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private Token token;
	private String trustId;
	private String trusteeUserId;
	private List<String> roles;

	@XmlElement(name = "is_admin")
	private Integer isAdmin;


	public Metadata() {
		super();
	}

	@Id
	@GeneratedValue
	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
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

	@Transient
	public List<Link> getRolesLinks() {
		return new ArrayList<Link>();
	}

	@Column(name = "ISADMIN", length = 1)
	public Integer getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}

}
