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
package com.infinities.keystone4j.common.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.model.common.MediaType;
import com.infinities.keystone4j.model.common.Version;
import com.infinities.keystone4j.model.common.VersionValuesWrapper;
import com.infinities.keystone4j.model.common.VersionWrapper;
import com.infinities.keystone4j.model.common.VersionsWrapper;

//keystone.controllers.Version 20141126
@Singleton
public class VersionApi {

	// private final static String DEFAULT_VERSION = "v2.0";
	private final static String MEDIA_TYPE_JSON = "application/vnd.openstack.identity-%s+json";
	// private final static String MEDIA_TYPE_XML =
	// "application/vnd.openstack.identity-%s+xml";
	private final static String V3 = "v3";
	private final static String V2 = "v2.0";
	private String endpointUrlType;
	// private final static String URL_POSTFIX = "{0}_endpoint";
	// private final static String PORT_POSTFIX = "{0}_port";
	private final static Logger logger = LoggerFactory.getLogger(VersionApi.class);


	// private final List<String> versions = Lists.newArrayList();

	public VersionApi(String versionType) {
		this.endpointUrlType = versionType;
	}

	protected URL getIdentityURL(ContainerRequestContext context, String version) throws MalformedURLException {
		String url = Wsgi.getBaseUrl(context, this.endpointUrlType);

		// String portKey = MessageFormat.format(PORT_POSTFIX, type);
		// String port = Config.getOpt(Config.Type.DEFAULT,
		// portKey).asText();
		// url = MessageFormat.format(url, port);

		if (!url.endsWith("/")) {
			url += "/";
		}
		url = url + version + "/";

		logger.debug("identityURL: {}", url);

		return new URL(url);
	}

	protected Map<String, Version> getVersionList(ContainerRequestContext context) throws MalformedURLException {
		Map<String, Version> versions = new HashMap<String, Version>();
		versions.put("v2.0", newV2Metadata(context));
		versions.put("v3", newV3Metadata(context));
		return versions;
	}

	private Version newV3Metadata(ContainerRequestContext context) throws MalformedURLException {
		Version metadata = new Version();
		metadata.setId("v3.0");
		metadata.setStatus("stable");
		metadata.setUpdated("2013-03-06T00:00:00Z");
		URL identityUrlV3 = getIdentityURL(context, V3);
		Link identity_link = new Link();
		identity_link.setRel("self");
		identity_link.setHref(identityUrlV3.toExternalForm());
		metadata.getLinks().add(identity_link);
		MediaType jsonType = new MediaType();
		jsonType.setBase("application/json");
		jsonType.setType(String.format(MEDIA_TYPE_JSON, V3, V3));
		metadata.getMediaTypes().add(jsonType);
		// MediaType xmlType = new MediaType();
		// xmlType.setBase("application/xml");
		// xmlType.setType(MessageFormat.format(MEDIA_TYPE_XML, V3));
		// metadata.getMediaTypes().add(xmlType);
		return metadata;
	}

	private Version newV2Metadata(ContainerRequestContext context) throws MalformedURLException {
		Version metadata = new Version();
		metadata.setId("v2.0");
		metadata.setStatus("stable");
		metadata.setUpdated("2014-04-17T00:00:00Z");
		URL identityUrl = getIdentityURL(context, V2);
		Link identity_link = new Link();
		identity_link.setRel("self");
		identity_link.setHref(identityUrl.toExternalForm());
		metadata.getLinks().add(identity_link);
		Link describeby1 = new Link();
		describeby1.setRel("describeby");
		describeby1.setType("text/html");
		describeby1.setHref("http://docs.openstack.org/api/openstack-identity-service/v2.0/content/");
		metadata.getLinks().add(describeby1);
		Link describeby2 = new Link();
		describeby2.setRel("describeby");
		describeby2.setType("application/pdf");
		describeby2.setHref("http://docs.openstack.org/api/openstack-identity-service/2.0/identity-dev-guide-2.0.pdf");
		metadata.getLinks().add(describeby2);
		MediaType jsonType = new MediaType();
		jsonType.setBase("application/json");
		jsonType.setType(String.format(MEDIA_TYPE_JSON, V2));
		metadata.getMediaTypes().add(jsonType);
		// MediaType xmlType = new MediaType();
		// xmlType.setBase("application/xml");
		// xmlType.setType(MessageFormat.format(MEDIA_TYPE_XML, V2));
		// metadata.getMediaTypes().add(xmlType);
		return metadata;
	}

	public Response getVersions(ContainerRequestContext context) throws MalformedURLException {
		Collection<Version> versions = getVersionList(context).values();
		VersionValuesWrapper valuesWrapper = new VersionValuesWrapper(versions);
		VersionsWrapper versionsWrapper = new VersionsWrapper(valuesWrapper);
		return Response.status(CustomResponseStatus.MULTIPLE_CHOICES).entity(versionsWrapper).build();
	}

	public VersionWrapper getVersionV3(ContainerRequestContext context) throws MalformedURLException {
		Collection<Version> versions = getVersionList(context).values();
		for (Version version : versions) {
			if ("v3.0".equals(version.getId())) {
				return new VersionWrapper(version);
			}
		}
		// TODO JSON_HOME? keystone.controllers.get_version_v3

		throw Exceptions.VersionNotFoundException.getInstance(null, V3);
	}

	public VersionWrapper getVersionV2(ContainerRequestContext context) throws MalformedURLException {
		Collection<Version> versions = getVersionList(context).values();
		for (Version version : versions) {
			if ("v2.0".equals(version.getId())) {
				return new VersionWrapper(version);
			}
		}
		throw Exceptions.VersionNotFoundException.getInstance(null, V2);
	}

	public String getEndpointUrlType() {
		return endpointUrlType;
	}

	public void setEndpointUrlType(String endpointUrlType) {
		this.endpointUrlType = endpointUrlType;
	}

}
