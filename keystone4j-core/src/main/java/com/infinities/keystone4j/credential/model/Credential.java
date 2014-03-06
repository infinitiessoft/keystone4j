package com.infinities.keystone4j.credential.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import com.infinities.keystone4j.BaseEntity;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.model.PolicyEntity;

@Entity
@Table(name = "CREDENTIAL", schema = "PUBLIC", catalog = "PUBLIC")
public class Credential extends BaseEntity implements java.io.Serializable, PolicyEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7583519542746277864L;
	private User user;
	private Project project;
	private String blob;
	private String type;
	private String extra;
	private boolean userUpdated = false;
	private boolean projectUpdated = false;
	private boolean blobUpdated = false;
	private boolean typeUpdated = false;
	private boolean extraUpdated = false;


	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID", nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		userUpdated = true;
	}

	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROJECTID")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		projectUpdated = true;
	}

	@Lob
	@Column(name = "BLOB", nullable = false)
	public String getBlob() {
		return blob;
	}

	public void setBlob(String blob) {
		this.blob = blob;
		blobUpdated = true;
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
	@Column(name = "EXTRA", nullable = false)
	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
		extraUpdated = true;
	}

	public boolean isUserUpdated() {
		return userUpdated;
	}

	public void setUserUpdated(boolean userUpdated) {
		this.userUpdated = userUpdated;
	}

	public boolean isProjectUpdated() {
		return projectUpdated;
	}

	public void setProjectUpdated(boolean projectUpdated) {
		this.projectUpdated = projectUpdated;
	}

	public boolean isBlobUpdated() {
		return blobUpdated;
	}

	public void setBlobUpdated(boolean blobUpdated) {
		this.blobUpdated = blobUpdated;
	}

	public boolean isTypeUpdated() {
		return typeUpdated;
	}

	public void setTypeUpdated(boolean typeUpdated) {
		this.typeUpdated = typeUpdated;
	}

	public boolean isExtraUpdated() {
		return extraUpdated;
	}

	public void setExtraUpdated(boolean extraUpdated) {
		this.extraUpdated = extraUpdated;
	}

	@XmlTransient
	@Override
	public Domain getDomain() {
		throw new IllegalStateException("propert 'domain' not exist");
	}

}
