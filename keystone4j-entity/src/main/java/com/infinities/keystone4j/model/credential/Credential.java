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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.infinities.keystone4j.model.BaseEntity;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "CREDENTIAL", schema = "PUBLIC", catalog = "PUBLIC")
public class Credential extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583519542746277864L;
	private String userId;
	private String projectId;
	private String blob;
	private String type;
	private String extra;
	private boolean userUpdated = false;
	private boolean projectUpdated = false;
	private boolean typeUpdated = false;
	private boolean extraUpdated = false;
	private boolean blobUpdated = false;


	@XmlElement(name = "user_id")
	@Column(name = "USERID", length = 64)
	public String getUserId() {
		return userId;
	}

	@XmlElement(name = "user_id")
	public void setUserId(String userId) {
		this.userId = userId;
		userUpdated = true;
	}

	@XmlElement(name = "project_id")
	@Column(name = "PROJECTID", length = 64)
	public String getProjectId() {
		return projectId;
	}

	@XmlElement(name = "project_id")
	public void setProjectId(String projectId) {
		this.projectId = projectId;
		projectUpdated = true;
	}

	// @OneToOne(optional = false, cascade = CascadeType.REMOVE)
	// @JoinColumn(name = "BLOBID", nullable = false)
	@Column(name = "BLOB", length = 512)
	public String getBlob() {
		return blob;
	}

	public void setBlob(String blob) {
		this.blob = blob;
		blobUpdated = true;
	}

	@Column(name = "TYPE", length = 255)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		typeUpdated = true;
	}

	@Lob
	@Column(name = "EXTRA", nullable = true)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@XmlTransient
	@Transient
	public boolean isUserUpdated() {
		return userUpdated;
	}

	public void setUserUpdated(boolean userUpdated) {
		this.userUpdated = userUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isProjectUpdated() {
		return projectUpdated;
	}

	public void setProjectUpdated(boolean projectUpdated) {
		this.projectUpdated = projectUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isTypeUpdated() {
		return typeUpdated;
	}

	public void setTypeUpdated(boolean typeUpdated) {
		this.typeUpdated = typeUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isBlobUpdated() {
		return blobUpdated;
	}

	public void setBlobUpdated(boolean blobUpdated) {
		this.blobUpdated = blobUpdated;
	}

}
