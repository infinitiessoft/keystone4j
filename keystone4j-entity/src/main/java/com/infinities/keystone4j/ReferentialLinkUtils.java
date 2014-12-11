package com.infinities.keystone4j;

import com.infinities.keystone4j.model.BaseEntity;
import com.infinities.keystone4j.model.common.Links;

@Deprecated
// replace by AbstractAction
public enum ReferentialLinkUtils {
	instance;

	public void addSelfReferentialLink(BaseEntity member, String baseUrl) {
		Links links = new Links();
		String self = baseUrl + member.getId();
		links.setSelf(self);
		member.setLinks(links);
	}

}
