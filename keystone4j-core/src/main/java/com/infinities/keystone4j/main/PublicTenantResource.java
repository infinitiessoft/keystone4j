package com.infinities.keystone4j.main;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.assignment.controller.TenantController;
import com.infinities.keystone4j.assignment.model.TenantsWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PublicTenantResource {

	private TenantController tenantController;


	public PublicTenantResource() {

	}

	@GET
	@Path("/")
	public TenantsWrapper getProjectsForToken() {
		return tenantController.getProjectsForToken();
	}

}
