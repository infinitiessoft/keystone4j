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
//package com.infinities.keystone4j.endpointfilter.controller.impl;
//
//import javax.inject.Inject;
//
//import org.glassfish.hk2.api.Factory;
//
//import com.infinities.keystone4j.assignment.AssignmentApi;
//import com.infinities.keystone4j.catalog.CatalogApi;
//import com.infinities.keystone4j.common.BaseControllerFactory;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;
//import com.infinities.keystone4j.policy.PolicyApi;
//import com.infinities.keystone4j.token.TokenApi;
//
//public class EndpointFilterControllerFactory extends BaseControllerFactory implements Factory<EndpointFilterController> {
//
//	private final AssignmentApi assignmentApi;
//	private final CatalogApi catalogApi;
//	private final EndpointFilterApi endpointFilterApi;
//	private final TokenApi tokenApi;
//	private final PolicyApi policyApi;
//
//
//	@Inject
//	public EndpointFilterControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi,
//			EndpointFilterApi endpointFilterApi, TokenApi tokenApi, PolicyApi policyApi) {
//		this.assignmentApi = assignmentApi;
//		this.catalogApi = catalogApi;
//		this.endpointFilterApi = endpointFilterApi;
//		this.tokenApi = tokenApi;
//		this.policyApi = policyApi;
//	}
//
//	@Override
//	public void dispose(EndpointFilterController arg0) {
//
//	}
//
//	@Override
//	public EndpointFilterController provide() {
//		EndpointFilterControllerImpl controller = new EndpointFilterControllerImpl(assignmentApi, catalogApi,
//				endpointFilterApi, tokenApi, policyApi);
//		controller.setRequest(getRequest());
//		return controller;
//	}
//
// }
