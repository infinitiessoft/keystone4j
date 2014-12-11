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
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.credential.CredentialWrapper;
import com.infinities.keystone4j.model.credential.CredentialsWrapper;
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
	@JsonView(Views.Basic.class)
	public Response createCredential(CredentialWrapper credentialWrapper) {
		return Response.status(Status.CREATED)
				.entity(credentialController.createCredential(credentialWrapper.getCredential())).build();
	}

	@GET
	@JsonView(Views.Basic.class)
	public CredentialsWrapper listCredentials(@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return credentialController.listCredentials(page, perPage);
	}

	@GET
	@Path("/{credentialid}")
	@JsonView(Views.Basic.class)
	public CredentialWrapper getCredential(@PathParam("credentialid") String credentialid) {
		return credentialController.getCredential(credentialid);
	}

	@PATCH
	@Path("/{credentialid}")
	@JsonView(Views.Basic.class)
	public CredentialWrapper updateCredential(@PathParam("credentialid") String credentialid, Credential credential) {
		return credentialController.updateCredential(credentialid, credential);
	}

	@DELETE
	@Path("/{credentialid}")
	public Response deleteCredential(@PathParam("credentialid") String credentialid) {
		credentialController.deleteCredential(credentialid);
		return Response.status(CustomResponseStatus.NO_CONTENT).build();
	}

}
