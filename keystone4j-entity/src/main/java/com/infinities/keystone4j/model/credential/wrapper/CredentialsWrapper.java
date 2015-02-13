package com.infinities.keystone4j.model.credential.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.utils.Views;

public class CredentialsWrapper implements CollectionWrapper<Credential> {

	private List<Credential> credentials;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public CredentialsWrapper() {

	}

	public CredentialsWrapper(List<Credential> credentials) {
		super();
		this.credentials = credentials;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
		this.links = links;
	}

	@JsonView(Views.All.class)
	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Credential> refs) {
		this.credentials = refs;
	}

	@XmlElement(name = "credentials")
	public List<Credential> getRefs() {
		return credentials;
	}
}
