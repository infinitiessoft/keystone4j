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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.endpointfilter.model.ProjectEndpoint;

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
	@XmlAttribute(name = "service_id")
	@NotNull(message = "service_id field is required and cannot be empty")
	@XmlIDREF
	private Service service;
	private String url;
	private String extra;
	private boolean legacyEndpointUpdated = false;
	private boolean interfaceTypeUpdated = false;
	private boolean regionUpdated = false;
	private boolean serviceUpdated = false;
	private boolean urlUpdated = false;
	private boolean extraUpdated = false;
	private Set<ProjectEndpoint> projectEndpoints = new HashSet<ProjectEndpoint>(0);


	@Column(name = "LEGACY_ENDPOINT", length = 64)
	public String getLegacyEndpoint() {
		return legacyEndpoint;
	}

	public void setLegacyEndpoint(String legacyEndpoint) {
		this.legacyEndpoint = legacyEndpoint;
		legacyEndpointUpdated = true;
	}

	@Column(name = "INTERFACE", length = 8, nullable = false)
	public String getInterfaceType() {
		return interfaceType;
	}

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICEID", nullable = false)
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
		serviceUpdated = true;
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

	@Lob
	@Column(name = "EXTRA")
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	@XmlID
	public String getEndpointId() {
		return this.getId();
	}

	public boolean isLegacyEndpointUpdated() {
		return legacyEndpointUpdated;
	}

	public void setLegacyEndpointUpdated(boolean legacyEndpointUpdated) {
		this.legacyEndpointUpdated = legacyEndpointUpdated;
	}

	public boolean isInterfaceTypeUpdated() {
		return interfaceTypeUpdated;
	}

	public void setInterfaceTypeUpdated(boolean interfaceTypeUpdated) {
		this.interfaceTypeUpdated = interfaceTypeUpdated;
	}

	public boolean isRegionUpdated() {
		return regionUpdated;
	}

	public void setRegionUpdated(boolean regionUpdated) {
		this.regionUpdated = regionUpdated;
	}

	public boolean isServiceUpdated() {
		return serviceUpdated;
	}

	public void setServiceUpdated(boolean serviceUpdated) {
		this.serviceUpdated = serviceUpdated;
	}

	public boolean isUrlUpdated() {
		return urlUpdated;
	}

	public void setUrlUpdated(boolean urlUpdated) {
		this.urlUpdated = urlUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	public Set<ProjectEndpoint> getProjectEndpoints() {
		return projectEndpoints;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "endpoint", cascade = CascadeType.ALL)
	public void setProjectEndpoints(Set<ProjectEndpoint> projectEndpoints) {
		this.projectEndpoints = projectEndpoints;
	}

}
