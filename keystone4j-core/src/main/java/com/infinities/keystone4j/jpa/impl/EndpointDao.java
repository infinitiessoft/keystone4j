package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.jpa.AbstractDao;

public class EndpointDao extends AbstractDao<Endpoint> {

	public EndpointDao() {
		super(Endpoint.class);
	}
}
