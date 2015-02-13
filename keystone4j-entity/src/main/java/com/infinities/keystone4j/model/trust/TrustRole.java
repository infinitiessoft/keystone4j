package com.infinities.keystone4j.model.trust;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.BaseEntity;

@Entity
@Table(name = "TRUST_ROLE", schema = "PUBLIC", catalog = "PUBLIC")
public class TrustRole extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7707735497658817097L;
	@XmlElement(name = "trust_id")
	private String trustId;
	@XmlElement(name = "role_id")
	private String roleId;


	public TrustRole() {

	}

	@Column(name = "TRUST_ID", length = 64, nullable = false)
	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

	@Column(name = "ROLE_ID", length = 64, nullable = false)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
