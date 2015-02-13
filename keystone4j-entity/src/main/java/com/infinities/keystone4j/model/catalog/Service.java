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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.utils.Views;

@Entity
@Table(name = "SERVICE", schema = "PUBLIC", catalog = "PUBLIC")
public class Service extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4200989587558979285L;
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0); // keystone.catalog.backends.sql.Endpoint
	// 20150112
	@NotNull(message = "type field is required and cannot be empty")
	private String type;
	private String name;
	private String extra;
	private Boolean enabled = true;

	private boolean typeUpdated = false;
	private boolean extraUpdated = false;
	private boolean nameUpdated = false;
	private boolean enabledUpdated = false;


	@JsonView(Views.Advance.class)
	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setNameUpdated(true);
	}

	@JsonView(Views.Basic.class)
	@Column(name = "TYPE", length = 255)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		typeUpdated = true;
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

	@JsonView(Views.Basic.class)
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
	public boolean isTypeUpdated() {
		return typeUpdated;
	}

	@XmlTransient
	@Transient
	public void setTypeUpdated(boolean typeUpdated) {
		this.typeUpdated = typeUpdated;
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

	@JsonView(Views.Advance.class)
	@Column(name = "ENABLED", nullable = false)
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		enabledUpdated = true;
	}

	@XmlTransient
	@Transient
	public boolean isEnabledUpdated() {
		return enabledUpdated;
	}

	public void setEnabledUpdated(boolean enabledUpdated) {
		this.enabledUpdated = enabledUpdated;
	}

}
