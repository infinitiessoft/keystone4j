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
//import java.util.List;
//import java.util.Map;
//
//import com.google.common.collect.Maps;
//import com.infinities.keystone4j.ProtectedAction;
//import com.infinities.keystone4j.assignment.AssignmentApi;
//import com.infinities.keystone4j.catalog.CatalogApi;
//import com.infinities.keystone4j.common.BaseController;
//import com.infinities.keystone4j.decorator.PaginateDecorator;
//import com.infinities.keystone4j.decorator.ProtectedDecorator;
//import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
//import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;
//import com.infinities.keystone4j.endpointfilter.controller.action.AddEndpointToProjectAction;
//import com.infinities.keystone4j.endpointfilter.controller.action.CheckEndpointToProjectAction;
//import com.infinities.keystone4j.endpointfilter.controller.action.ListEndpointsForProjectAction;
//import com.infinities.keystone4j.endpointfilter.controller.action.ListProjectsForEndpointAction;
//import com.infinities.keystone4j.endpointfilter.controller.action.RemoveEndpointToProjectAction;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.ProjectsWrapper;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//import com.infinities.keystone4j.model.catalog.EndpointsWrapper;
//import com.infinities.keystone4j.policy.PolicyApi;
//import com.infinities.keystone4j.token.TokenApi;
//
//public class EndpointFilterControllerImpl extends BaseController implements EndpointFilterController {
//
//	private final AssignmentApi assignmentApi;
//	private final CatalogApi catalogApi;
//	private final EndpointFilterApi endpointFilterApi;
//	private final TokenApi tokenApi;
//	private final PolicyApi policyApi;
//	private final Map<String, Object> parMap;
//
//
//	public EndpointFilterControllerImpl(AssignmentApi assignmentApi, CatalogApi catalogApi,
//			EndpointFilterApi endpointFilterApi, TokenApi tokenApi, PolicyApi policyApi) {
//		super();
//		this.assignmentApi = assignmentApi;
//		this.catalogApi = catalogApi;
//		this.endpointFilterApi = endpointFilterApi;
//		this.tokenApi = tokenApi;
//		this.policyApi = policyApi;
//		parMap = Maps.newHashMap();
//	}
//
//	@Override
//	public ProjectsWrapper listProjectsForEndpoint(String endpointid, int page, int perPage) {
//		parMap.put("endpointid", endpointid);
//		ProtectedAction<List<Project>> command = new ProtectedDecorator<List<Project>>(new PaginateDecorator<Project>(
//				new ListProjectsForEndpointAction(assignmentApi, catalogApi, endpointFilterApi, endpointid), page, perPage),
//				null, tokenApi, policyApi, parMap);
//		List<Project> ret = command.execute(getRequest());
//		return new ProjectsWrapper(ret, getRequest());
//	}
//
//	@Override
//	public void addEndpointToProject(String projectid, String endpointid) {
//		parMap.put("projectid", projectid);
//		parMap.put("endpointid", endpointid);
//		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new AddEndpointToProjectAction(assignmentApi,
//				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
//		command.execute(getRequest());
//	}
//
//	@Override
//	public void checkEndpointInProject(String projectid, String endpointid) {
//		parMap.put("projectid", projectid);
//		parMap.put("endpointid", endpointid);
//		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new CheckEndpointToProjectAction(assignmentApi,
//				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
//		command.execute(getRequest());
//	}
//
//	@Override
//	public EndpointsWrapper listEndpointsForProject(String endpointid, int page, int perPage) {
//		parMap.put("endpointid", endpointid);
//		ProtectedAction<List<Endpoint>> command = new ProtectedDecorator<List<Endpoint>>(new PaginateDecorator<Endpoint>(
//				new ListEndpointsForProjectAction(assignmentApi, catalogApi, endpointFilterApi, endpointid), page, perPage),
//				null, tokenApi, policyApi, parMap);
//		List<Endpoint> ret = command.execute(getRequest());
//		return new EndpointsWrapper(ret, getRequest());
//	}
//
//	@Override
//	public void removeEndpointFromProject(String projectid, String endpointid) {
//		parMap.put("projectid", projectid);
//		parMap.put("endpointid", endpointid);
//		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new RemoveEndpointToProjectAction(assignmentApi,
//				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
//		command.execute(getRequest());
//	}
//
// }
