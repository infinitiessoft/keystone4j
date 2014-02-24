package com.infinities.keystone4j.admin;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.assignment.controller.RoleController;
import com.infinities.keystone4j.assignment.controller.TenantController;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.assignment.model.TenantWrapper;
import com.infinities.keystone4j.assignment.model.TenantsWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TenantResource {

	private RoleController roleController;
	private TenantController tenantController;


	public TenantResource() {

	}

	@GET
	@Path("/")
	public TenantsWrapper listTenants() {
		return tenantController.getAllProjects();
	}

	@GET
	@Path("/{tenantid}")
	public TenantWrapper getUser(@PathParam("tenantid") String tenantid) {
		return tenantController.getProject(tenantid);
	}

	@GET
	@Path("/{tenantid}/users/{userid}/roles")
	public RolesWrapper getUserRoles(@PathParam("tenantid") String tenantid, @PathParam("userid") String userid) {
		return roleController.getUserRoles(userid, tenantid);
	}

}
