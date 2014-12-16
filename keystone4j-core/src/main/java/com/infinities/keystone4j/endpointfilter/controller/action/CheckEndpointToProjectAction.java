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
