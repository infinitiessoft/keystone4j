package com.infinities.keystone4j.model.identity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UpdateUserParam implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436954503286770674L;
	private final User user = new User();


	public String getName() {
		return user.getName();
	}

	public void setName(String name) {
		this.user.setName(name);
	}

	public String getPassword() {
		return user.getPassword();
	}

	public void setPassword(String password) {
		user.setPassword(password);
	}

	public Boolean getEnabled() {
		return user.getEnabled();
	}

	public void setEnabled(Boolean enabled) {
		user.setEnabled(enabled);
	}

	public String getDefaultProjectId() {
		return user.getDefaultProjectId();
	}

	@XmlElement(name = "default_project_id")
	public void setDefaultProjectId(String defaultProjectId) {
		user.setDefaultProjectId(defaultProjectId);
	}

	public String getDescription() {
		return user.getDescription();
	}

	public void setDescription(String description) {
		this.user.setDescription(description);
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

}
