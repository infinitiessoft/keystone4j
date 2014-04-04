package com.infinities.keystone4j.jpa.impl;

import com.infinities.keystone4j.jpa.AbstractDao;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class EndpointDao extends AbstractDao<Endpoint> {

	public EndpointDao() {
		super(Endpoint.class);
	}
}
