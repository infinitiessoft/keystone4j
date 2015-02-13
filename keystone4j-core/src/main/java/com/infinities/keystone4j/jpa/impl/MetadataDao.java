package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.token.Metadata;

public class MetadataDao extends AbstractDao<Metadata> {

	public MetadataDao() {
		super(Metadata.class);
	}

}
