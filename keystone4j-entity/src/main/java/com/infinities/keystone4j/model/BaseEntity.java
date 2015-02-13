package com.infinities.keystone4j.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

//import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.common.MemberLinks;
import com.infinities.keystone4j.model.utils.Views;

@MappedSuperclass
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8270011464801370690L;
	private String id;
	private String description;
	private int version;
	private boolean descriptionUpdated = false;
	private MemberLinks links = new MemberLinks();


	@Column(name = "DESC", length = 150)
	@JsonView(Views.Advance.class)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		setDescriptionUpdated(true);

	}

	@XmlTransient
	@Version
	@Column(name = "OPTLOCK")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@JsonView(Views.Basic.class)
	@Id
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}

	@Transient
	@XmlTransient
	public boolean isDescriptionUpdated() {
		return descriptionUpdated;
	}

	@Transient
	@XmlTransient
	public void setDescriptionUpdated(boolean descriptionUpdated) {
		this.descriptionUpdated = descriptionUpdated;
	}

	@JsonView(Views.Advance.class)
	@Transient
	public MemberLinks getLinks() {
		return links;
	}

	@Transient
	public void setLinks(MemberLinks links) {
		this.links = links;
	}

}
