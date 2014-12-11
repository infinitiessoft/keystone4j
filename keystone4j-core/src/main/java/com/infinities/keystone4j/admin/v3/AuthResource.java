package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthV3Wrapper;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.utils.Views;

//keystone.auth.routers 20141210

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AuthResource.class);
	// private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final AuthController authController;


	@Inject
	public AuthResource(AuthController authController) {
		this.authController = authController;
	}

	@POST
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response authenticateForToken(AuthV3Wrapper authWrapper) throws Exception {
		return authController.authenticateForToken(authWrapper.getAuth());
	}

	@HEAD
	@Path("/tokens")
	public Response checkToken() throws Exception {
		return authController.checkToken();
	}

	@DELETE
	@Path("/tokens")
	public Response revokeToken() throws Exception {
		authController.revokeToken();
		return Response.noContent().build();
	}

	@GET
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response validateToken() throws Exception {
		return authController.validateToken();
	}

	@GET
	@Path("/tokens/OS-PKI/revoked")
	public MemberWrapper<String> getRevocationList() throws Exception {
		return authController.getRevocationList();
	}

	@GET
	@Path("/catalog")
	public MemberWrapper<Catalog> getAuthCatalog() throws Exception {
		return authController.getAuthCatalog();
	}

	@GET
	@Path("/projects")
	public CollectionWrapper<Project> getAuthProjects() throws Exception {
		return authController.getAuthProjects();
	}

	@GET
	@Path("/domains")
	public CollectionWrapper<Domain> getAuthDomains() throws Exception {
		return authController.getAuthDomains();
	}

}
