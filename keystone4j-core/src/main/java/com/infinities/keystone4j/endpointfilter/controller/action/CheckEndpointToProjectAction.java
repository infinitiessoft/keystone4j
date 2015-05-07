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
//package com.infinities.keystone4j.endpointfilter.controller.action;
//
//import javax.ws.rs.container.ContainerRequestContext;
//
//import com.infinities.keystone4j.assignment.AssignmentApi;
//import com.infinities.keystone4j.catalog.CatalogApi;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//
//public class CheckEndpointToProjectAction extends AbstractEndpointFilterAction<Endpoint> {
//
//	private final String endpointid;
//	private final String projectid;
//
//
//	public CheckEndpointToProjectAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
//			EndpointFilterApi endpointFilterApi, String projectid, String endpointid) {
//		super(assignmentApi, catalogApi, endpointFilterApi);
//		this.projectid = projectid;
//		this.endpointid = endpointid;
//	}
//
//	@Override
//	public Endpoint execute(ContainerRequestContext request) {
//		this.getCatalogApi().getEndpoint(endpointid);
//		this.getAssignmentApi().getProject(projectid);
//		this.getEndpointFilterApi().checkEndpointInProject(endpointid, projectid);
//		return null;
//	}
//
//	@Override
//	public String getName() {
//		return "check_endpoint_in_project";
//	}
// }
