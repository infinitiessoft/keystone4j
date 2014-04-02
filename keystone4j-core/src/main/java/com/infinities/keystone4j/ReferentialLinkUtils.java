package com.infinities.keystone4j;

import com.infinities.keystone4j.common.model.Links;

public enum ReferentialLinkUtils {
	instance;

	public void addSelfReferentialLink(BaseEntity member, String baseUrl) {
		Links links = new Links();
		String self = baseUrl + member.getId();
		links.setSelf(self);
		member.setLinks(links);
	}

}
