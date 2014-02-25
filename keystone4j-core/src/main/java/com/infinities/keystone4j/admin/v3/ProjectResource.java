package com.infinities.keystone4j.admin.v3;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.ProjectWrapper;
import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.assignment.model.RolesWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class ProjectResource {

	private final RoleV3Controller roleController;
	private final ProjectV3Controller projectController;


	public ProjectResource(RoleV3Controller roleController, ProjectV3Controller projectController) {
		this.roleController = roleController;
		this.projectController = projectController;
	}

	@POST
	public ProjectWrapper createProject(HttpServletRequest request, Project project) {
		return projectController.createProject(project);
	}

	@GET
	public ProjectsWrapper listProject(@QueryParam("domain_id") String domainid, @QueryParam("name") String name,
			@QueryParam("enabled") Boolean enabled, @DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return projectController.listProjects(domainid, name, enabled, page, perPage);
	}

	@GET
	@Path("/{projectid}")
	public ProjectWrapper getProject(@PathParam("projectid") String projectid) {
		return projectController.getProject(projectid);
	}

	@PATCH
	@Path("/{projectid}")
	public ProjectWrapper updateProject(@PathParam("projectid") String projectid, Project project) {
		return projectController.updateProject(projectid, project);
	}

	@DELETE
	@Path("/{projectid}")
	public Response deleteProject(@PathParam("projectid") String projectid) {
		projectController.deleteProject(projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response createGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByUserProject(roleid, userid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@PUT
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response createGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.createGrantByGroupProject(roleid, groupid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response checkGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByUserProject(roleid, userid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@HEAD
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response checkGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.checkGrantByGroupProject(roleid, groupid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public RolesWrapper listGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByUserProject(userid, projectid, page, perPage);
	}

	@GET
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public RolesWrapper listGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@DefaultValue("1") @QueryParam("page") int page, @DefaultValue("30") @QueryParam("per_page") int perPage) {
		return roleController.listGrantsByGroupProject(groupid, projectid, page, perPage);
	}

	@DELETE
	@Path("/{projectid}/users/{userid}/roles/{roleid}")
	public Response revokeGrantByUser(@PathParam("projectid") String projectid, @PathParam("userid") String userid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByUserProject(roleid, userid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{projectid}/groups/{groupid}/roles/{roleid}")
	public Response revokeGrantByGroup(@PathParam("projectid") String projectid, @PathParam("groupid") String groupid,
			@PathParam("roleid") String roleid) {
		roleController.revokeGrantByGroupProject(roleid, groupid, projectid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}
}
