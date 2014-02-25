package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.trust.model.SignedWrapper;

public class AuthResource {

	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final AuthController authController;


	public AuthResource(AuthController authController) {
		this.authController = authController;
	}

	@POST
	@Path("/tokens")
	public Response authenticateForToken(AuthV3 auth) {
		TokenMetadata token = authController.authenticateForToken(auth);
		return Response.status(CustomResponseStatus.CREATE_TOKEN).entity(token.getTokenData())
				.header(SUBJECT_TOKEN_HEADER, token.getTokenid()).build();
	}

	@HEAD
	@Path("/tokens")
	public Response checkToken() {
		authController.checkToken();
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@DELETE
	@Path("/tokens")
	public Response revokeToken() {
		authController.revokeToken();
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/tokens")
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
