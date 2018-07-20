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
package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;

//keystone.contrib.simple_cert.routers 20141129
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimpleCertResource {

	private final SimpleCertV3Controller simpleCertV3Controller;


	@Inject
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
