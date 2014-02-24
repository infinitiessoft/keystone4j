package com.infinities.keystone4j.admin;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.infinities.keystone4j.common.model.ExtensionApi;
import com.infinities.keystone4j.common.model.ExtensionWrapper;
import com.infinities.keystone4j.common.model.ExtensionsWrapper;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExtensionResource {

	// module in keystone will register for extension
	private ExtensionApi extensionApi;


	public ExtensionResource() {
		this.extensionApi = new ExtensionApi();
	}

	@GET
	@Path("/")
	public ExtensionsWrapper listExtension() {
		return extensionApi.getExtensionsInfo();
	}

	@GET
	@Path("/{alias}")
	public ExtensionWrapper getExtension(@PathParam("alias") String alias) {
		return extensionApi.getExtensionInfo(alias);
	}

}
