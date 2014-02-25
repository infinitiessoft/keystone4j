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
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.catalog.model.ServiceWrapper;
import com.infinities.keystone4j.catalog.model.ServicesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class ServiceResource {

	private final ServiceV3Controller serviceController;


	public ServiceResource(ServiceV3Controller serviceController) {
		this.serviceController = serviceController;
	}

	@POST
	public ServiceWrapper createService(Service service) {
		return serviceController.createService(service);
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
	public ServiceWrapper updateService(@PathParam("serviceid") String serviceid, Service service) {
		return serviceController.updateService(serviceid, service);
	}

	@DELETE
	@Path("/{serviceid}")
	public Response deleteService(@PathParam("serviceid") String serviceid) {
		serviceController.deleteService(serviceid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
