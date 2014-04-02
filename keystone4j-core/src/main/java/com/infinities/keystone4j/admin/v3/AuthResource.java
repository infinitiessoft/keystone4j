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
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.AuthV3Wrapper;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.trust.model.SignedWrapper;
import com.infinities.keystone4j.utils.jackson.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

	private final static Logger logger = LoggerFactory.getLogger(AuthResource.class);
	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final AuthController authController;


	@Inject
	public AuthResource(AuthController authController) {
		this.authController = authController;
	}

	@POST
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response authenticateForToken(AuthV3Wrapper authWrapper) {
		TokenMetadata token = authController.authenticateForToken(authWrapper.getAuth());
		logger.debug("assign X-Subjecj-Token: {}", token.getTokenid());
		return Response.status(Status.CREATED).entity(token.getTokenData()).header(SUBJECT_TOKEN_HEADER, token.getTokenid())
				.build();
	}

	@HEAD
	@Path("/tokens")
	public Response checkToken() {
		authController.checkToken();
		// return Response.ok().build();
		return Response.noContent().build();
	}

	@DELETE
	@Path("/tokens")
	public Response revokeToken() {
		authController.revokeToken();
		return Response.noContent().build();
	}

	@GET
	@Path("/tokens")
	@JsonView(Views.AuthenticateForToken.class)
	public Response validateToken() {
		TokenMetadata token = authController.validateToken();
		return Response.status(CustomResponseStatus.VALIDATE_TOKEN).entity(token.getTokenData())
				.header(SUBJECT_TOKEN_HEADER, token.getTokenid()).build();
	}

	@GET
	@Path("/tokens/OS-PKI/revoked")
	public SignedWrapper getRevocationList() {
		return authController.getRevocationList();
	}

}
