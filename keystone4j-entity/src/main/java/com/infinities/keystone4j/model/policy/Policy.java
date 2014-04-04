package com.infinities.keystone4j.model.policy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.infinities.keystone4j.model.BaseEntity;

@Entity
@Table(name = "POLICY", schema = "PUBLIC", catalog = "PUBLIC")
public class Policy extends BaseEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8725006425714289308L;
	@NotNull(message = "blob field is required and cannot be empty")
	private String blob;
	@NotNull(message = "type field is required and cannot be empty")
	private String type;
	private String extra;

	private boolean blobUpdated = false;
	private boolean typeUpdated = false;
	private boolean extraUpdated = false;


	@Lob
	@Column(name = "BLOB", nullable = false)
	public String getBlob() {
		return blob;
	}

	public void setBlob(String blob) {
		this.blob = blob;
		blobUpdated = true;
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

}
