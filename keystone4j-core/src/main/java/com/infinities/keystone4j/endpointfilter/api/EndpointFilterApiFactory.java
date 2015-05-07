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
//package com.infinities.keystone4j.endpointfilter.api;
//
//import javax.inject.Inject;
//
//import org.glassfish.hk2.api.Factory;
//
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
//import com.infinities.keystone4j.extension.ExtensionApi;
//
//public class EndpointFilterApiFactory implements Factory<EndpointFilterApi> {
//
//	private final EndpointFilterDriver endpointFilterDriver;
//	private final ExtensionApi extensionApi;
//
//
//	@Inject
//	public EndpointFilterApiFactory(EndpointFilterDriver endpointFilterDriver, ExtensionApi extensionApi) {
//		this.endpointFilterDriver = endpointFilterDriver;
//		this.extensionApi = extensionApi;
//	}
//
//	@Override
//	public void dispose(EndpointFilterApi arg0) {
//
//	}
//
//	@Override
//	public EndpointFilterApi provide() {
//		return new EndpointFilterApiImpl(endpointFilterDriver, extensionApi);
//	}
//
// }
