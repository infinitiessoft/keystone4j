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
//import java.util.List;
//
//import javax.ws.rs.container.ContainerRequestContext;
//
//import com.infinities.keystone4j.assignment.AssignmentApi;
//import com.infinities.keystone4j.catalog.CatalogApi;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.model.assignment.Project;
//
//public class ListProjectsForEndpointAction extends AbstractEndpointFilterAction<List<Project>> {
//
//	private final String endpointid;
//
//
//	public ListProjectsForEndpointAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
//			EndpointFilterApi endpointFilterApi, String endpointid) {
//		super(assignmentApi, catalogApi, endpointFilterApi);
//		this.endpointid = endpointid;
//	}
//
//	@Override
//	public List<Project> execute(ContainerRequestContext request) {
//		return this.getEndpointFilterApi().listProjectsForEndpoint(endpointid);
//	}
//
//	@Override
//	public String getName() {
//		return "list_projects_for_endpoint";
//	}
// }
