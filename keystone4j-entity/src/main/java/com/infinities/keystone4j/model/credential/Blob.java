package com.infinities.keystone4j.model.credential;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonInclude;

//@Entity
//@Table(name = "CREDENTIAL_BLOB", schema = "PUBLIC", catalog = "PUBLIC")
public class Blob implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583519542746277864L;
	private String access;
	private String secret;
	private String trustId;


	// private Credential credential;
	// private boolean accessUpdated = false;
	// private boolean secretUpdated = false;

	// @Column(name = "ACCESS", length = 255)
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
		// accessUpdated = true;
	}

	// @Column(name = "SECRET", length = 255)
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
		// secretUpdated = true;
	}

	// @XmlTransient
	// @Transient
	// public boolean isAccessUpdated() {
	// return accessUpdated;
	// }
	//
	// public void setAccessUpdated(boolean accessUpdated) {
	// this.accessUpdated = accessUpdated;
	// }
	//
	// @XmlTransient
	// @Transient
	// public boolean isSecretUpdated() {
	// return secretUpdated;
	// }
	//
	// public void setSecretUpdated(boolean secretUpdated) {
	// this.secretUpdated = secretUpdated;
	// }

	// @XmlTransient
	// @OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "blob")
	// public Credential getCredential() {
	// return credential;
	// }
	//
	// @XmlTransient
	// public void setCredential(Credential credential) {
	// this.credential = credential;
	// }

	// @Column(name = "TRUSTID", length = 64)
	@JsonInclude
	@XmlElement(name = "trust_id")
	public String getTrustId() {
		return trustId;
	}

	public void setTrustId(String trustId) {
		this.trustId = trustId;
	}

}
