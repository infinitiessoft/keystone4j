package com.infinities.keystone4j.model;

import java.util.List;

import com.infinities.keystone4j.model.common.Links;

public interface CollectionWrapper<T> extends Wrapper<T> {

	Links getLinks();

	void setLinks(Links links);

	boolean isTruncated();

	void setTruncated(boolean truncated);

	void setRefs(List<T> refs);

}
