package com.infinities.keystone4j.model.policy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.utils.JsonUtils;

@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "POLICY", schema = "PUBLIC", catalog = "PUBLIC")
public class Policy extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8725006425714289308L;
	@NotNull(message = "blob field is required and cannot be empty")
	private Map<String, Object> blob = new HashMap<String, Object>(0);
	@NotNull(message = "type field is required and cannot be empty")
	private String type;
	private String extra;

	private String userId;
	private String projectId;

	private boolean blobUpdated = false;
	private boolean typeUpdated = false;
	private boolean extraUpdated = false;
	private boolean userUpdated = false;
	private boolean projectUpdated = false;


	@Transient
	public Map<String, Object> getBlob() {
		return blob;
	}

	@Transient
	public void setBlob(Map<String, Object> blob) {
		this.blob = blob;
		blobUpdated = true;
	}

	@Lob
	@XmlTransient
	@Column(name = "BLOB", nullable = false)
	public String getBlobStr() throws JsonGenerationException, JsonMappingException, IOException {
		return JsonUtils.toJsonWithoutPrettyPrint(blob);
	}

	@XmlTransient
	public void setBlobStr(String blobStr) throws IOException {
		this.blob = JsonUtils.readJson(blobStr, new TypeReference<Map<String, Object>>() {
		});
	}

	@Column(name = "TYPE", length = 255, nullable = false)
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

	@XmlTransient
	@Transient
	public boolean isBlobUpdated() {
		return blobUpdated;
	}

	public void setBlobUpdated(boolean blobUpdated) {
		this.blobUpdated = blobUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isTypeUpdated() {
		return typeUpdated;
	}

	public void setTypeUpdated(boolean typeUpdated) {
		this.typeUpdated = typeUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlElement(name = "user_id")
	@Column(name = "USERID", length = 64)
	public String getUserId() {
		return userId;
	}

	@XmlElement(name = "user_id")
	public void setUserId(String userId) {
		this.userId = userId;
		userUpdated = true;
	}

	@XmlElement(name = "project_id")
	@Column(name = "PROJECTID", length = 64)
	public String getProjectId() {
		return projectId;
	}

	@XmlElement(name = "project_id")
	public void setProjectId(String projectId) {
		this.projectId = projectId;
		projectUpdated = true;
	}

	@XmlTransient
	@Transient
	public boolean isProjectUpdated() {
		return projectUpdated;
	}

	public void setProjectUpdated(boolean projectUpdated) {
		this.projectUpdated = projectUpdated;
	}

	@XmlTransient
	@Transient
	public boolean isUserUpdated() {
		return userUpdated;
	}

	public void setUserUpdated(boolean userUpdated) {
		this.userUpdated = userUpdated;
	}

}
