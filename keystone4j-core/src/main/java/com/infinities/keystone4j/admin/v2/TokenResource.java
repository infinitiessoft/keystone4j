package com.infinities.keystone4j.admin.v2;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.token.AuthWrapper;
import com.infinities.keystone4j.model.token.v2.EndpointsV2Wrapper;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.token.controller.TokenController;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {

	private final static Logger logger = LoggerFactory.getLogger(TokenResource.class);
	private final TokenController tokenController;


	@Inject
	public TokenResource(TokenController tokenController) {
		this.tokenController = tokenController;
	}

	@POST
	@JsonView(Views.AuthenticateForToken.class)
	public Response authenticate(AuthWrapper authWrapper) {
		TokenV2DataWrapper wrapper = tokenController.authenticate(authWrapper.getAuth());
		logger.debug("assign X-Subjecj-Token: {}", wrapper.getAccess().getToken().getId());
		return Response.ok(wrapper).header("Vary", "X-Auth-Token").build();
	}

	@GET
	@Path("/revoked")
	public Response getRevocationList() {
		SignedWrapper wrapper = tokenController.getRevocationList();
		logger.debug("assign X-Subjecj-Token: {}", wrapper.getSigned());
		return Response.ok(wrapper).header("Vary", "X-Auth-Token").build();
	}

	@GET
	@Path("{tokenid}")
	public Response validateToken(@PathParam("tokenid") String tokenid, @QueryParam("belongsTo") String belongsTo) {
		TokenV2DataWrapper wrapper = tokenController.validateToken(tokenid, belongsTo);
		return Response.ok(wrapper).header("Vary", "X-Auth-Token").build();
	}

	@HEAD
	@Path("{tokenid}")
	public Response validateTokenHead(@PathParam("tokenid") String tokenid, @QueryParam("belongsTo") String belongsTo) {
		tokenController.validateTokenHead(tokenid, belongsTo);
		return Response.status(Status.NO_CONTENT).header("Vary", "X-Auth-Token").build();
	}

	@DELETE
	@Path("{tokenid}")
	public Response deleteToken(@PathParam("tokenid") String tokenid) {
		tokenController.deleteToken(tokenid);
		return Response.noContent().header("Vary", "X-Auth-Token").build();
	}

	@GET
	@Path("{tokenid}/endpoints")
	public Response getEndpoints(@PathParam("tokenid") String tokenid) {
		EndpointsV2Wrapper wrapper = tokenController.getEndpoints(tokenid);
		return Response.ok(wrapper).header("Vary", "X-Auth-Token").build();
	}

}
