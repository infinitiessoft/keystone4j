package com.infinities.keystone4j.admin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.token.controller.TokenController;

@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class CertificateResource {

	private TokenController tokenController;


	@GET
	@Path("/ca")
	public Response getCaCert() {
		return Response.ok(tokenController.getCaCert(), MediaType.APPLICATION_OCTET_STREAM).build();
	}

	@GET
	@Path("/signing")
	public Response getSigningCert() {
		return Response.ok(tokenController.getSigningCert(), MediaType.APPLICATION_OCTET_STREAM).build();
	}
}
