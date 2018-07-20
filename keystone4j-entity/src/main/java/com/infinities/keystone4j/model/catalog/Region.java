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
package com.infinities.keystone4j.model.catalog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "REGION")
public class Region extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4200989587558979285L;
	// @NotNull(message = "type field is required and cannot be empty")
	private String url;
	private String name;
	private String extra;
	private String parentRegionId;
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0); // keystone.catalog.backends.sql.Endpoint
																// 20150112
	private boolean urlUpdated = false;
	private boolean extraUpdated = false;
	private boolean nameUpdated = false;


	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setNameUpdated(true);
	}

	@Column(name = "URL", length = 255, nullable = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		urlUpdated = true;
	}

	@XmlTransient
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "region", cascade = CascadeType.ALL)
	public Set<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(Set<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	//
	// @XmlID
	// @Transient
	// public String getServiceId() {
	// return this.getId();
	// }

	@XmlTransient
	@Transient
	public boolean isUrlUpdated() {
		return urlUpdated;
	}

	@XmlTransient
	@Transient
	public void setUrlUpdated(boolean urlUpdated) {
		this.urlUpdated = urlUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	@XmlTransient
	@Transient
	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isNameUpdated() {
		return nameUpdated;
	}

	@XmlTransient
	@Transient
	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
	}

	@Column(name = "PARENT_REGION_ID", length = 255, nullable = true)
	public String getParentRegionId() {
		return parentRegionId;
	}

	public void setParentRegionId(String parentRegionId) {
		this.parentRegionId = parentRegionId;
	}

}
