/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
