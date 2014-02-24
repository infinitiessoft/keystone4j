package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.catalog.model.OSKSADMServiceWrapper;
import com.infinities.keystone4j.catalog.model.OSKSADMServicesWrapper;

public interface ServiceController {

	OSKSADMServicesWrapper getServices();

	OSKSADMServiceWrapper getService();

	void deleteService();

	OSKSADMServiceWrapper createService();

}
