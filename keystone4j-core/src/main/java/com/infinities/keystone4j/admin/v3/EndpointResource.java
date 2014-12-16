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
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.EndpointWrapper;
import com.infinities.keystone4j.model.utils.Views;

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
	public Response createEndpoint(EndpointWrapper endpointWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(endpointController.createEndpoint(endpointWrapper.getEndpoint()))
				.build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Endpoint> listEndpoints(@QueryParam("interface") String interfaceType,
			@QueryParam("service_id") String serviceid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return endpointController.listEndpoints();
	}

	@GET
	@Path("/{endpointid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Endpoint> getEndpoint(@PathParam("endpointid") String endpointid) throws Exception {
		return endpointController.getEndpoint(endpointid);
	}

	@PATCH
	@Path("/{endpointid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Endpoint> updateEndpoint(@PathParam("endpointid") String endpointid, EndpointWrapper endpointWrapper)
			throws Exception {
		return endpointController.updateEndpoint(endpointid, endpointWrapper.getEndpoint());
	}

	@DELETE
	@Path("/{endpointid}")
	public Response deleteEndpoint(@PathParam("endpointid") String endpointid) throws Exception {
		endpointController.deleteEndpoint(endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
