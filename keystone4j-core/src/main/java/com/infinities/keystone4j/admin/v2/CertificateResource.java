/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
	public Response getCaCertficate() throws Exception {
		return tokenController.getCaCert();
	}

	@GET
	@Path("/signing")
	public Response listCertificates() throws Exception {
		return tokenController.getSigningCert();
	}

}
