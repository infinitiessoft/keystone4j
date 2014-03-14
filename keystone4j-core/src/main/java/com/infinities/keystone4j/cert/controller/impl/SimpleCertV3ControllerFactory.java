package com.infinities.keystone4j.cert.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.extension.ExtensionApi;

public class SimpleCertV3ControllerFactory extends BaseControllerFactory implements Factory<SimpleCertV3Controller> {

	private final ExtensionApi extensionApi;


	@Inject
	public SimpleCertV3ControllerFactory(ExtensionApi extensionApi) {
		this.extensionApi = extensionApi;
	}

	@Override
	public void dispose(SimpleCertV3Controller arg0) {

	}

	@Override
	public SimpleCertV3Controller provide() {
		SimpleCertV3ControllerImpl controller = new SimpleCertV3ControllerImpl(extensionApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
