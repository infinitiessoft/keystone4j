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
import com.infinities.keystone4j.catalog.controller.RegionV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.model.catalog.RegionWrapper;
import com.infinities.keystone4j.model.utils.Views;

//keystone.catalog.routers 20141211

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RegionResource {

	private final RegionV3Controller regionController;


	@Inject
	public RegionResource(RegionV3Controller regionController) {
		this.regionController = regionController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createRegion(RegionWrapper regionWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(regionController.createRegion(regionWrapper.getRegion())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public CollectionWrapper<Region> listRegions(@QueryParam("type") String type,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return regionController.listRegions();
	}

	@GET
	@Path("/{regionid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Region> getRegion(@PathParam("regionid") String regionid) throws Exception {
		return regionController.getRegion(regionid);
	}

	@PATCH
	@Path("/{regionid}")
	@JsonView(Views.Basic.class)
	public MemberWrapper<Region> updateRegion(@PathParam("regionid") String regionid, RegionWrapper regionWrapper)
			throws Exception {
		return regionController.updateRegion(regionid, regionWrapper.getRegion());
	}

	@DELETE
	@Path("/{regionid}")
	public Response deleteRegion(@PathParam("regionid") String regionid) throws Exception {
		regionController.deleteRegion(regionid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
