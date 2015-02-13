package com.infinities.keystone4j.model.trust.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.CollectionLinks;
import com.infinities.keystone4j.model.trust.Trust;

public class TrustsWrapper implements CollectionWrapper<Trust> {

	private List<Trust> trusts;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public TrustsWrapper() {

	}

	public TrustsWrapper(List<Trust> trusts) {
		this.trusts = trusts;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
		this.links = links;
	}

	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Trust> refs) {
		this.trusts = refs;
	}

	@XmlElement(name = "trusts")
	public List<Trust> getRefs() {
		return trusts;
	}

}
