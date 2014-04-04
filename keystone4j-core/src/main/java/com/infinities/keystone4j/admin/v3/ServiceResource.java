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
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.catalog.ServiceWrapper;
import com.infinities.keystone4j.model.catalog.ServicesWrapper;
import com.infinities.keystone4j.model.utils.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceResource {

	private final ServiceV3Controller serviceController;


	@Inject
	public ServiceResource(ServiceV3Controller serviceController) {
		this.serviceController = serviceController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createService(ServiceWrapper serviceWrapper) {
		return Response.status(Status.CREATED).entity(serviceController.createService(serviceWrapper.getService())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public ServicesWrapper listServices(@QueryParam("type") String type, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return serviceController.listServices(type, page, perPage);
	}

	@GET
	@Path("/{serviceid}")
	@JsonView(Views.Basic.class)
	public ServiceWrapper getService(@PathParam("serviceid") String serviceid) {
		return serviceController.getService(serviceid);
	}

	@PATCH
	@Path("/{serviceid}")
	@JsonView(Views.Basic.class)
	public ServiceWrapper updateService(@PathParam("serviceid") String serviceid, ServiceWrapper serviceWrapper) {
		return serviceController.updateService(serviceid, serviceWrapper.getService());
	}

	@DELETE
	@Path("/{serviceid}")
	public Response deleteService(@PathParam("serviceid") String serviceid) {
		serviceController.deleteService(serviceid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
