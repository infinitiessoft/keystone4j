package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.model.assignment.wrapper.ProjectWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.ProjectsWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.model.utils.Views;

//keystone.assignment.routers 20141209

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProjectResource {

	private final RoleV3Controller roleController;
	private final ProjectV3Controller projectController;


	@Inject
	public ProjectResource(RoleV3Controller roleController, ProjectV3Controller projectController) {
		this.roleController = roleController;
		this.projectController = projectController;
	}

	@POST
	@JsonView(Views.Basic.class)
	public Response createProject(ProjectWrapper projectWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(projectController.createProject(projectWrapper.getRef())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public ProjectsWrapper listProject(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@QueryParam("enabled") Boolean enabled, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (ProjectsWrapper) projectController.listProjects();
	}

	@GET
	@Path("/{projectid}")
	@JsonView(Views.Basic.class)
	public ProjectWrapper getProject(@PathParam("projectid") String projectid) throws Exception {
		return (ProjectWrapper) projectController.getProject(projectid);
	}

	@PATCH
	@Path("/{projectid}")
	@JsonView(Views.Basic.class)
	public ProjectWrapper updateProject(@PathParam("projectid") String projectid, ProjectWrapper projectWrapper)
			throws Exception {
		return (ProjectWrapper) projectController.updateProject(projectid, projectWrapper.getRef());
	}

	@DELETE
	@Path("/{projectid}")
	public Response deleteProject(@PathParam("projectid") String projectid) throws Exception {
		projectController.deleteProject(projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{projectid}/users/{userid}/roles")
	@JsonView(Views.Basic.class)
	public RolesWrapper listGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (RolesWrapper) roleController.listGrants(userid, null, null, projectid);
	}

	@PUT
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response createGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, userid, null, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response checkGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, userid, null, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response revokeGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, userid, null, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	// @GET
	// @Path("/{projectid}/users")
	// @JsonView(Views.Basic.class)
	// public CollectionWrapper<Project> getProjectUsers(@PathParam("projectid")
	// String projectid,
	// @QueryParam("name") String name, @QueryParam("enabled") Boolean enabled,
	// @DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30")
	// @QueryParam("per_page") int perPage) {
	// return projectController.getProjectUsers(projectid, enabled, name, page,
	// perPage);
	// }

	@PUT
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response createGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.createGrant(roleid, null, groupid, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response checkGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.checkGrant(roleid, null, groupid, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{projectid}/groups/{groupid}/roles")
	@JsonView(Views.Basic.class)
	public RolesWrapper listGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage)
			throws Exception {
		return (RolesWrapper) roleController.listGrants(null, groupid, null, projectid);
	}

	@DELETE
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response revokeGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) throws Exception {
		roleController.revokeGrant(roleid, null, groupid, null, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}
}
