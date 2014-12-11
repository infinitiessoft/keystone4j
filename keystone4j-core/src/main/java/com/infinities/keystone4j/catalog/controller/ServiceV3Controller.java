package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Service;

public interface ServiceV3Controller {

	MemberWrapper<Service> createService(Service service) throws Exception;

	CollectionWrapper<Service> listServices() throws Exception;

	MemberWrapper<Service> getService(String serviceid) throws Exception;

	MemberWrapper<Service> updateService(String serviceid, Service service) throws Exception;

	void deleteService(String serviceid) throws Exception;

}
