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
import com.infinities.keystone4j.model.catalog.wrapper.ServiceWrapper;
import com.infinities.keystone4j.model.catalog.wrapper.ServicesWrapper;
import com.infinities.keystone4j.model.utils.Views;

//keystone.catalog.routers 20141211

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ServiceResource {

	private final ServiceV3Controller serviceController;


	@Inject
	public ServiceResource(ServiceV3Controller serviceController) {
		this.serviceController = serviceController;
	}

	@POST
	@JsonView(Views.Advance.class)
	public Response createService(ServiceWrapper serviceWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(serviceController.createService(serviceWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public ServicesWrapper listServices(@QueryParam("type") String type, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (ServicesWrapper) serviceController.listServices();
	}

	@GET
	@Path("/{serviceid}")
	@JsonView(Views.Advance.class)
	public ServiceWrapper getService(@PathParam("serviceid") String serviceid) throws Exception {
		return (ServiceWrapper) serviceController.getService(serviceid);
	}

	@PATCH
	@Path("/{serviceid}")
	@JsonView(Views.Advance.class)
	public ServiceWrapper updateService(@PathParam("serviceid") String serviceid, ServiceWrapper serviceWrapper)
			throws Exception {
		return (ServiceWrapper) serviceController.updateService(serviceid, serviceWrapper.getRef());
	}

	@DELETE
	@Path("/{serviceid}")
	public Response deleteService(@PathParam("serviceid") String serviceid) throws Exception {
		serviceController.deleteService(serviceid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
