package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;

public class SimpleCertResource {

	private final SimpleCertV3Controller simpleCertV3Controller;


	public SimpleCertResource(SimpleCertV3Controller simpleCertV3Controller) {
		this.simpleCertV3Controller = simpleCertV3Controller;
	}

	@GET
	@Path("/ca")
	public Response getCaCertficate() {
		return simpleCertV3Controller.getCaCertificate();
	}

	@GET
	@Path("/certificates")
	public Response listCertificates() {
		return simpleCertV3Controller.listCertificate();
	}

}
