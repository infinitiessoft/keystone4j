package com.infinities.keystone4j.admin.v3;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.model.credential.wrapper.CredentialWrapper;
import com.infinities.keystone4j.model.credential.wrapper.CredentialsWrapper;
import com.infinities.keystone4j.model.utils.Views;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CredentialResource {

	private final CredentialV3Controller credentialController;


	@Inject
	public CredentialResource(CredentialV3Controller credentialController) {
		this.credentialController = credentialController;
	}

	@POST
	@JsonView(Views.Advance.class)
	public Response createCredential(CredentialWrapper credentialWrapper) throws Exception {
		return Response.status(Status.CREATED).entity(credentialController.createCredential(credentialWrapper.getRef()))
				.build();
	}

	@GET
	@JsonView(Views.Advance.class)
	public CredentialsWrapper listCredentials(@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) throws Exception {
		return (CredentialsWrapper) credentialController.listCredentials();
	}

	@GET
	@Path("/{credentialid}")
	@JsonView(Views.Advance.class)
	public CredentialWrapper getCredential(@PathParam("credentialid") String credentialid) throws Exception {
		return (CredentialWrapper) credentialController.getCredential(credentialid);
	}

	@PATCH
	@Path("/{credentialid}")
	@JsonView(Views.Advance.class)
	public CredentialWrapper updateCredential(@PathParam("credentialid") String credentialid,
			CredentialWrapper credentialWrapper) throws Exception {
		return (CredentialWrapper) credentialController.updateCredential(credentialid, credentialWrapper.getRef());
	}

	@DELETE
	@Path("/{credentialid}")
	public Response deleteCredential(@PathParam("credentialid") String credentialid) throws Exception {
		credentialController.deleteCredential(credentialid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
