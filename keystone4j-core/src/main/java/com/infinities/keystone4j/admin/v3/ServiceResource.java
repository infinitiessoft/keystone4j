package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.catalog.model.ServiceWrapper;
import com.infinities.keystone4j.catalog.model.ServicesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class ServiceResource {

	private final ServiceV3Controller serviceController;


	@Inject
	public ServiceResource(ServiceV3Controller serviceController) {
		this.serviceController = serviceController;
	}

	@POST
	public Response createService(ServiceWrapper serviceWrapper) {
		return Response.status(Status.CREATED).entity(serviceController.createService(serviceWrapper.getService())).build();
	}

	@GET
	public ServicesWrapper listServices(@QueryParam("type") String type, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return serviceController.listServices(type, page, perPage);
	}

	@GET
	@Path("/{serviceid}")
	public ServiceWrapper getService(@PathParam("serviceid") String serviceid) {
		return serviceController.getService(serviceid);
	}

	@PATCH
	@Path("/{serviceid}")
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
