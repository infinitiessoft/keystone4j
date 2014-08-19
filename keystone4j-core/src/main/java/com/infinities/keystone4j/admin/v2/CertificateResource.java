package com.infinities.keystone4j.admin.v2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.token.controller.TokenController;

@Produces(MediaType.TEXT_HTML)
public class CertificateResource {

	private final TokenController tokenController;


	public CertificateResource(TokenController tokenController) {
		super();
		this.tokenController = tokenController;
	}

	@GET
	@Path("/ca")
	public Response getCaCertficate() {
		return tokenController.getCaCert();
	}

	@GET
	@Path("/signing")
	public Response listCertificates() {
		return tokenController.getSigningCert();
	}

}
