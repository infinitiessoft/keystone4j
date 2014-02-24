package com.infinities.keystone4j.admin;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.token.controller.TokenController;
import com.infinities.keystone4j.token.model.Auth;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenWrapper;
import com.infinities.keystone4j.trust.model.SignedWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {

	private TokenController tokenController;


	@POST
	@Path("/")
	public TokenWrapper authenticate(Auth auth) {
		return tokenController.authenticate(auth);
	}

	@GET
	@Path("/revoked")
	public SignedWrapper getRevocationList() {
		return tokenController.getRevocationList();
	}

	@GET
	@Path("/{tokenid}")
	public Token validateToken(@PathParam("tokenid") String tokenid) {
		return tokenController.validateToken();
	}

	@HEAD
	@Path("/{tokenid}")
	public Token validateTokenHead(@PathParam("tokenid") String tokenid) {
		return tokenController.validateTokenHead();
	}

	@DELETE
	@Path("/{tokenid}")
	public Response deleteToken(@PathParam("tokenid") String tokenid) {
		tokenController.deleteToken();
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

	@GET
	@Path("/{tokenid}/endpoints")
	public EndpointsWrapper listEndpoints(@PathParam("tokenid") String tokenid) {
		return tokenController.getEndpoints();
	}
}
