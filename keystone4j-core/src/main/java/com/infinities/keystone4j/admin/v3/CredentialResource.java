package com.infinities.keystone4j.admin.v3;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.PATCH;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.credential.model.CredentialWrapper;
import com.infinities.keystone4j.credential.model.CredentialsWrapper;

public class CredentialResource {

	private CredentialV3Controller credentialController;


	@POST
	public CredentialWrapper createCredential(Credential credential) {
		return credentialController.createCredential(credential);
	}

	@GET
	public CredentialsWrapper listCredentials(@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("30") @QueryParam("per_page") int perPage) {
		return credentialController.listCredentials(page, perPage);
	}

	@GET
	@Path("/{credentialid}")
	public CredentialWrapper getCredential(@PathParam("credentialid") String credentialid) {
		return credentialController.getCredential(credentialid);
	}

	@PATCH
	@Path("/{credentialid}")
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
