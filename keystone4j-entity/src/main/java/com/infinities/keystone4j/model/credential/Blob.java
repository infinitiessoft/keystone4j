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
