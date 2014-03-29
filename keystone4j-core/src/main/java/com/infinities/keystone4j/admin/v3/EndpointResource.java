package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.Views;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EndpointResource {

	private final EndpointV3Controller endpointController;


	@Inject
	public EndpointResource(EndpointV3Controller endpointController) {
		this.endpointController = endpointController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createEndpoint(EndpointWrapper endpointWrapper) {
		return Response.status(Status.CREATED).entity(endpointController.createEndpoint(endpointWrapper.getEndpoint()))
				.build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public EndpointsWrapper listEndpoints(@QueryParam("interface") String interfaceType,
			@QueryParam("service_id") String serviceid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return endpointController.listEndpoints(interfaceType, serviceid, page, perPage);
	}

	@GET
	@Path("/{endpointid}")
	@JsonView(Views.Basic.class)
	public EndpointWrapper getEndpoint(@PathParam("endpointid") String endpointid) {
		return endpointController.getEndpoint(endpointid);
	}

	@PATCH
	@Path("/{endpointid}")
	@JsonView(Views.Basic.class)
	public EndpointWrapper updateEndpoint(@PathParam("endpointid") String endpointid, EndpointWrapper endpointWrapper) {
		return endpointController.updateEndpoint(endpointid, endpointWrapper.getEndpoint());
	}

	@DELETE
	@Path("/{endpointid}")
	public Response deleteEndpoint(@PathParam("endpointid") String endpointid) {
		endpointController.deleteEndpoint(endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
