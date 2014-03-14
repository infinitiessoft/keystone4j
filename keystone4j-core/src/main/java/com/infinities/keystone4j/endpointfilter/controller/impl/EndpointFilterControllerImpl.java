package com.infinities.keystone4j.endpointfilter.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
import com.infinities.keystone4j.endpointfilter.action.AddEndpointToProjectAction;
import com.infinities.keystone4j.endpointfilter.action.CheckEndpointToProjectAction;
import com.infinities.keystone4j.endpointfilter.action.ListEndpointsForProjectAction;
import com.infinities.keystone4j.endpointfilter.action.ListProjectsForEndpointAction;
import com.infinities.keystone4j.endpointfilter.action.RemoveEndpointToProjectAction;
import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class EndpointFilterControllerImpl extends BaseController implements EndpointFilterController {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final EndpointFilterApi endpointFilterApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public EndpointFilterControllerImpl(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, TokenApi tokenApi, PolicyApi policyApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.endpointFilterApi = endpointFilterApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public ProjectsWrapper listProjectsForEndpoint(String endpointid, int page, int perPage) {
		parMap.put("endpointid", endpointid);
		Action<List<Project>> command = new PolicyCheckDecorator<List<Project>>(new PaginateDecorator<Project>(
				new ListProjectsForEndpointAction(assignmentApi, catalogApi, endpointFilterApi, endpointid), page, perPage),
				null, tokenApi, policyApi, parMap);
		List<Project> ret = command.execute(getRequest());
		return new ProjectsWrapper(ret);
	}

	@Override
	public void addEndpointToProject(String projectid, String endpointid) {
		parMap.put("projectid", projectid);
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new AddEndpointToProjectAction(assignmentApi,
				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public void checkEndpointInProject(String projectid, String endpointid) {
		parMap.put("projectid", projectid);
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new CheckEndpointToProjectAction(assignmentApi,
				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public EndpointsWrapper listEndpointsForProject(String endpointid, int page, int perPage) {
		parMap.put("endpointid", endpointid);
		Action<List<Endpoint>> command = new PolicyCheckDecorator<List<Endpoint>>(new PaginateDecorator<Endpoint>(
				new ListEndpointsForProjectAction(assignmentApi, catalogApi, endpointFilterApi, endpointid), page, perPage),
				null, tokenApi, policyApi, parMap);
		List<Endpoint> ret = command.execute(getRequest());
		return new EndpointsWrapper(ret);
	}

	@Override
	public void removeEndpointFromProject(String projectid, String endpointid) {
		parMap.put("projectid", projectid);
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new RemoveEndpointToProjectAction(assignmentApi,
				catalogApi, endpointFilterApi, projectid, endpointid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
