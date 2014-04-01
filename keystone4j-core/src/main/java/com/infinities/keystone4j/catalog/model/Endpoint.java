package com.infinities.keystone4j.catalog.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Strings;
import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.endpointfilter.model.ProjectEndpoint;
import com.infinities.keystone4j.utils.jackson.Views;

@Entity
@Table(name = "ENDPOINT", schema = "PUBLIC", catalog = "PUBLIC")
public class Endpoint extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7446373184746636927L;
	private String legacyEndpoint;
	@NotNull(message = "interface field is required and cannot be empty")
	private String interfaceType;
	private String region;
	@NotNull(message = "service_id field is required and cannot be empty")
	private Service service;
	private String url;
	private String extra;
	private String name;
	private boolean legacyEndpointUpdated = false;
	private boolean interfaceTypeUpdated = false;
	private boolean regionUpdated = false;
	private boolean serviceUpdated = false;
	private boolean urlUpdated = false;
	private boolean extraUpdated = false;
	private boolean nameUpdated = false;
	private Set<ProjectEndpoint> projectEndpoints = new HashSet<ProjectEndpoint>(0);


	@XmlTransient
	@Column(name = "LEGACY_ENDPOINT", length = 64)
	public String getLegacyEndpoint() {
		return legacyEndpoint;
	}

	@XmlTransient
	public void setLegacyEndpoint(String legacyEndpoint) {
		this.legacyEndpoint = legacyEndpoint;
		legacyEndpointUpdated = true;
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

	@Column(name = "REGION", length = 255)
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
		regionUpdated = true;
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
		if (!Strings.isNullOrEmpty(serviceid)) {
			Service service = new Service();
			service.setId(serviceid);
			setService(service);
		}
	}

	@Lob
	@Column(name = "URL", nullable = false)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		urlUpdated = true;
	}

	@XmlTransient
	@Lob
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
	public boolean isLegacyEndpointUpdated() {
		return legacyEndpointUpdated;
	}

	@XmlTransient
	@Transient
	public void setLegacyEndpointUpdated(boolean legacyEndpointUpdated) {
		this.legacyEndpointUpdated = legacyEndpointUpdated;
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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoint", cascade = CascadeType.ALL)
	public Set<ProjectEndpoint> getProjectEndpoints() {
		return projectEndpoints;
	}

	@JsonView(Views.All.class)
	public void setProjectEndpoints(Set<ProjectEndpoint> projectEndpoints) {
		this.projectEndpoints = projectEndpoints;
	}

	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		nameUpdated = true;
	}

}
