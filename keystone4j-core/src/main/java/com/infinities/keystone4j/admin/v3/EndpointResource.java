package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class EndpointResource {

	private EndpointV3Controller endpointController;


	@POST
	public EndpointWrapper createEndpoint(Endpoint endpoint) {
		return endpointController.createEndpoint(endpoint);
	}

	@GET
	public EndpointsWrapper listEndpoints(@QueryParam("interface") String interfaceType,
			@QueryParam("service_id") String serviceid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return endpointController.listEndpoints(interfaceType, serviceid, page, perPage);
	}

	@GET
	@Path("/{endpointid}")
	public EndpointWrapper getEndpoint(@PathParam("endpointid") String endpointid) {
		return endpointController.getEndpoint(endpointid);
	}

	@PATCH
	@Path("/{endpointid}")
	public EndpointWrapper updateEndpoint(@PathParam("endpointid") String endpointid, Endpoint endpoint) {
		return endpointController.updateEndpoint(endpointid, endpoint);
	}

	@DELETE
	@Path("/{endpointid}")
	public Response deleteEndpoint(@PathParam("endpointid") String endpointid) {
		endpointController.deleteEndpoint(endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
