package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;
import com.infinities.keystone4j.model.assignment.ProjectsWrapper;
import com.infinities.keystone4j.model.catalog.EndpointsWrapper;

public class EndpointFilterResource {

	private final EndpointFilterController endpointFilterController;


	public EndpointFilterResource(EndpointFilterController endpointFilterController) {
		this.endpointFilterController = endpointFilterController;
	}

	@GET
	@Path("/endpoints/{endpointid}/projects")
	public ProjectsWrapper listProjectsForEndpoint(@PathParam("endpointid") String endpointid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return endpointFilterController.listProjectsForEndpoint(endpointid, page, perPage);
	}

	@PUT
	@Path("/projects/{projectid}/endpoints/{endpointid}")
	public Response addEndpointToProject(@PathParam("projectid") String projectid, @PathParam("endpointid") String endpointid) {
		endpointFilterController.addEndpointToProject(projectid, endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/projects/{projectid}/endpoints/{endpointid}")
	public Response checkEndpointToProject(@PathParam("projectid") String projectid,
			@PathParam("endpointid") String endpointid) {
		endpointFilterController.checkEndpointInProject(projectid, endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/projects/{projectid}/endpoints")
	public EndpointsWrapper listEndpointForProject(@PathParam("endpointid") String endpointid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return endpointFilterController.listEndpointsForProject(endpointid, page, perPage);
	}

	@DELETE
	@Path("/projects/{projectid}/endpoints/{endpointid}")
	public Response removeEndpointFromProject(@PathParam("projectid") String projectid,
			@PathParam("endpointid") String endpointid) {
		endpointFilterController.removeEndpointFromProject(projectid, endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
