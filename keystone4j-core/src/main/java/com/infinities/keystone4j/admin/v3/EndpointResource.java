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
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointsWrapper;
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
	@JsonView(Views.Advance.class)
	public Response createEndpoint(EndpointWrapper endpointWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(endpointController.createEndpoint(endpointWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public EndpointsWrapper listEndpoints(@QueryParam("interface") String interfaceType,
			@QueryParam("service_id") String serviceid, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (EndpointsWrapper) endpointController.listEndpoints();
	}

	@GET
	@Path("/{endpointid}")
	@JsonView(Views.Advance.class)
	public EndpointWrapper getEndpoint(@PathParam("endpointid") String endpointid) throws Exception {
		return (EndpointWrapper) endpointController.getEndpoint(endpointid);
	}

	@PATCH
	@Path("/{endpointid}")
	@JsonView(Views.Advance.class)
	public EndpointWrapper updateEndpoint(@PathParam("endpointid") String endpointid, EndpointWrapper endpointWrapper)
			throws Exception {
		return (EndpointWrapper) endpointController.updateEndpoint(endpointid, endpointWrapper.getRef());
	}

	@DELETE
	@Path("/{endpointid}")
	public Response deleteEndpoint(@PathParam("endpointid") String endpointid) throws Exception {
		endpointController.deleteEndpoint(endpointid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
