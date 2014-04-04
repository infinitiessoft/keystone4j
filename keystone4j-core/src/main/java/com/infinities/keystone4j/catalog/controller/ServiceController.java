package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.catalog.OSKSADMServiceWrapper;
import com.infinities.keystone4j.model.catalog.OSKSADMServicesWrapper;

public interface ServiceController {

	OSKSADMServicesWrapper getServices();

	OSKSADMServiceWrapper getService();

	void deleteService();

	OSKSADMServiceWrapper createService();

}
