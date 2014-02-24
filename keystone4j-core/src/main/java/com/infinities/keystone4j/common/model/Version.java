package com.infinities.keystone4j.common.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Version implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8371864459227315816L;
	private String id;
	private String status;
	private String updated;
	private List<Link> links;
	@XmlElement(name = "media-types")
	private List<MediaType> mediaTypes;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public List<MediaType> getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(List<MediaType> mediaTypes) {
		this.mediaTypes = mediaTypes;
	}

}
