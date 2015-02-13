package com.infinities.keystone4j.model;

import java.util.List;

import com.infinities.keystone4j.model.common.CollectionLinks;

public interface CollectionWrapper<T> extends Wrapper<T> {

	CollectionLinks getLinks();

	void setLinks(CollectionLinks links);

	boolean isTruncated();

	void setTruncated(boolean truncated);

	void setRefs(List<T> refs);

}
