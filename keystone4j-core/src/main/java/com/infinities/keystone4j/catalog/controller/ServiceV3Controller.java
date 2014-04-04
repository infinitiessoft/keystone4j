package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.ServiceWrapper;
import com.infinities.keystone4j.model.catalog.ServicesWrapper;

public interface ServiceV3Controller {

	ServiceWrapper createService(Service service);

	ServicesWrapper listServices(String type, int page, int perPage);

	ServiceWrapper getService(String serviceid);

	ServiceWrapper updateService(String serviceid, Service service);

	void deleteService(String serviceid);

}
