package com.infinities.keystone4j.admin;

import java.net.MalformedURLException;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.common.model.VersionApi;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminVersionApiResource {

	private VersionApi versionApi;


	public AdminVersionApiResource(Properties properties) {
		versionApi = new VersionApi(properties, "admin");
	}

	@GET
	public Response getVersions() throws MalformedURLException {
		return versionApi.getVersions();
	}

}
