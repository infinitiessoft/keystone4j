package com.infinities.keystone4j.catalog.model;

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

import com.infinities.keystone4j.BaseEntity;

@Entity
@Table(name = "SERVICE", schema = "PUBLIC", catalog = "PUBLIC")
public class Service extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4200989587558979285L;
	@NotNull(message = "type field is required and cannot be empty")
	private String type;
	private String name;
	private String extra;
	private Set<Endpoint> endpoints = new HashSet<Endpoint>(0);
	private boolean typeUpdated = false;
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

	@Column(name = "TYPE", length = 255)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		typeUpdated = true;
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

}
