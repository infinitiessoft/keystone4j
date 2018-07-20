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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "ENDPOINT")
public class Endpoint extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7446373184746636927L;
	@NotNull(message = "interface field is required and cannot be empty")
	private String interfaceType;
	private String legacyEndpointId;
	@NotNull(message = "service_id field is required and cannot be empty")
	private Service service; // keystone.catalog.backends.sql.Endpoint 20150112
	private String url;
	private String extra;
	private String name;
	private boolean legacyEndpointIdUpdated = false;
	private boolean interfaceTypeUpdated = false;
	private boolean regionUpdated = false;
	private boolean serviceUpdated = false;
	private boolean urlUpdated = false;
	private boolean extraUpdated = false;
	private boolean nameUpdated = false;
	private Boolean enabled = true;
	private boolean enabledUpdate = false;
	// private Set<ProjectEndpoint> projectEndpoints = new
	// HashSet<ProjectEndpoint>(0);
	private Region region;// keystone.catalog.backends.sql.Endpoint 20150112


	@XmlTransient
	@Column(name = "LEGACY_ENDPOINT_ID", length = 64)
	public String getLegacyEndpointId() {
		return legacyEndpointId;
	}

	@XmlTransient
	public void setLegacyEndpointId(String legacyEndpointId) {
		this.legacyEndpointId = legacyEndpointId;
		legacyEndpointIdUpdated = true;
	}

	@XmlElement(name = "interface")
	@Column(name = "INTERFACE", length = 8, nullable = false)
	public String getInterfaceType() {
		return interfaceType;
	}

	@XmlElement(name = "interface")
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
		interfaceTypeUpdated = true;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REGIONID", nullable = true)
	public Region getRegion() {
		return region;
	}

	@XmlTransient
	public void setRegion(Region region) {
		this.region = region;
		regionUpdated = true;
	}

	@JsonView(Views.All.class)
	@Transient
	@XmlElement(name = "region_id")
	public String getRegionid() {
		if (getRegion() != null) {
			return getRegion().getId();
		}
		return null;
	}

	@Transient
	@XmlElement(name = "region_id")
	public void setRegionid(String regionid) {
		if (!(regionid == null || regionid.length() == 0)) {
			Region region = new Region();
			region.setId(regionid);
			setRegion(region);
		}
	}

	@Transient
	@XmlElement(name = "region")
	public void setRegion(String region) {
		if (!(region == null || region.length() == 0)) {
			Region ref = new Region();
			ref.setId(region);
			setRegion(ref);
		}
	}

	@Transient
	@XmlElement(name = "region")
	public String getRegionId() {
		if (getRegion() != null) {
			return getRegion().getId();
		}
		return null;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICEID", nullable = false)
	public Service getService() {
		return service;
	}

	@XmlTransient
	public void setService(Service service) {
		this.service = service;
		serviceUpdated = true;
	}

	@JsonView(Views.Advance.class)
	@Transient
	@XmlElement(name = "service_id")
	public String getServiceid() {
		if (getService() != null) {
			return getService().getId();
		}
		return null;
	}

	@Transient
	@XmlElement(name = "service_id")
	public void setServiceid(String serviceid) {
		if (!(serviceid == null || serviceid.length() == 0)) {
			Service service = new Service();
			service.setId(serviceid);
			setService(service);
		}
	}

	@Column(name = "URL", nullable = false)
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

	// @JsonView(Views.All.class)
	// @XmlID
	// @Transient
	// public String getEndpointId() {
	// return this.getId();
	// }

	@XmlTransient
	@Transient
	public boolean isLegacyEndpointIdUpdated() {
		return legacyEndpointIdUpdated;
	}

	@XmlTransient
	@Transient
	public void setLegacyEndpointIdUpdated(boolean legacyEndpointIdUpdated) {
		this.legacyEndpointIdUpdated = legacyEndpointIdUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isInterfaceTypeUpdated() {
		return interfaceTypeUpdated;
	}

	@XmlTransient
	@Transient
	public void setInterfaceTypeUpdated(boolean interfaceTypeUpdated) {
		this.interfaceTypeUpdated = interfaceTypeUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isRegionUpdated() {
		return regionUpdated;
	}

	@XmlTransient
	@Transient
	public void setRegionUpdated(boolean regionUpdated) {
		this.regionUpdated = regionUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isServiceUpdated() {
		return serviceUpdated;
	}

	@XmlTransient
	@Transient
	public void setServiceUpdated(boolean serviceUpdated) {
		this.serviceUpdated = serviceUpdated;
	}

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
	public boolean isNameUpdated() {
		return nameUpdated;
	}

	@XmlTransient
	@Transient
	public void setNameUpdated(boolean nameUpdated) {
		this.nameUpdated = nameUpdated;
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

	// @JsonView(Views.All.class)
	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoint", cascade =
	// CascadeType.ALL)
	// public Set<ProjectEndpoint> getProjectEndpoints() {
	// return projectEndpoints;
	// }
	//
	// @JsonView(Views.All.class)
	// public void setProjectEndpoints(Set<ProjectEndpoint> projectEndpoints) {
	// this.projectEndpoints = projectEndpoints;
	// }

	@JsonView(Views.Advance.class)
	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

	@JsonView(Views.Advance.class)
	@Column(name = "ENABLED", length = 8, nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	@XmlTransient
	public void setEnabled(Boolean enabled) {
		setEnabledUpdate(true);
		this.enabled = enabled;
	}

	@XmlTransient
	@Transient
	public boolean isEnabledUpdate() {
		return enabledUpdate;
	}

	@XmlTransient
	@Transient
	public void setEnabledUpdate(boolean enabledUpdate) {
		this.enabledUpdate = enabledUpdate;
	}

}
