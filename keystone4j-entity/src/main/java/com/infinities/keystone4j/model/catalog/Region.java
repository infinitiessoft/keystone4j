package com.infinities.keystone4j.model.catalog;

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
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "REGION", schema = "PUBLIC", catalog = "PUBLIC")
public class Region extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4200989587558979285L;
	@NotNull(message = "type field is required and cannot be empty")
	private String url;
	private String name;
	private String extra;
	private Region parentRegion;
	private Set<Region> childRegions = new HashSet<Region>(0);
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0);
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

	@Column(name = "URL", length = 255)
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

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_REGIONID", nullable = false)
	public Region getParentRegion() {
		return parentRegion;
	}

	public void setParentRegion(Region parentRegion) {
		this.parentRegion = parentRegion;
	}

	@JsonView(Views.All.class)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentRegion", cascade = CascadeType.ALL)
	public Set<Region> getChildRegions() {
		return childRegions;
	}

	public void setChildRegions(Set<Region> childRegions) {
		this.childRegions = childRegions;
	}

}
