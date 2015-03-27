package com.infinities.keystone4j.model.identity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

public class CreateUserParam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	@XmlElement(name = "domain_id")
	private String domainId; // keystone.identity.backends.sql.User 20150114
	@NotNull(message = "name field is required and cannot be empty")
	private String name;
	private String password;
	private Boolean enabled = true;
	@XmlElement(name = "default_project_id")
	private String defaultProjectId;
	private String description;


	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getDefaultProjectId() {
		return defaultProjectId;
	}

	public void setDefaultProjectId(String defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
