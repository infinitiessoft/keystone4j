package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;

public interface EndpointV3Controller {

	MemberWrapper<Endpoint> createEndpoint(Endpoint endpoint) throws Exception;

	CollectionWrapper<Endpoint> listEndpoints() throws Exception;

	MemberWrapper<Endpoint> getEndpoint(String endpointid) throws Exception;

	MemberWrapper<Endpoint> updateEndpoint(String endpointid, Endpoint endpoint) throws Exception;

	void deleteEndpoint(String endpointid) throws Exception;

}
