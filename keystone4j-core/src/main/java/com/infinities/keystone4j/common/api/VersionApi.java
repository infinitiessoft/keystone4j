package com.infinities.keystone4j.common.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.model.common.MediaType;
import com.infinities.keystone4j.model.common.Version;
import com.infinities.keystone4j.model.common.VersionWrapper;
import com.infinities.keystone4j.model.common.VersionsWrapper;

@Singleton
public class VersionApi {

	// private final static String DEFAULT_VERSION = "v2.0";
	private final static String MEDIA_TYPE_JSON = "application/vnd.openstack.identity-{0}+json";
	private final static String MEDIA_TYPE_XML = "application/vnd.openstack.identity-{0}+xml";
	private final static String V3 = "v3";
	private String type;
	private final static String URL_POSTFIX = "{0}_endpoint";
	// private final static String PORT_POSTFIX = "{0}_port";
	private final static Logger logger = LoggerFactory.getLogger(VersionApi.class);


	// private final List<String> versions = Lists.newArrayList();

	public VersionApi(String type) {
		this.setType(type);
	}

	public VersionApi() {
		this("public");
	}

	protected URL getIdentityURL(String version) throws MalformedURLException {
		String urlKey = MessageFormat.format(URL_POSTFIX, type);
		String url = Config.Instance.getOpt(Config.Type.DEFAULT, urlKey).asText();

		// String portKey = MessageFormat.format(PORT_POSTFIX, type);
		// String port = Config.Instance.getOpt(Config.Type.DEFAULT,
		// portKey).asText();
		// url = MessageFormat.format(url, port);

		if (!url.endsWith("/")) {
			url += "/";
		}
		url = url + version + "/";

		logger.debug("identityURL: {}", url);

		return new URL(url);
	}

	protected List<Version> getVersionList() throws MalformedURLException {
		List<Version> versionMetadatas = Lists.newArrayList();
		// if (versions.contains(V3)) {
		versionMetadatas.add(newV3Metadata());
		// }
		return versionMetadatas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private Version newV3Metadata() throws MalformedURLException {
		Version metadata = new Version();
		metadata.setId("v3.0");
		metadata.setStatus("stable");
		metadata.setUpdated("2013-03-06T00:00:00Z");
		URL identityUrlV3 = getIdentityURL(V3);
		Link identity_link = new Link();
		identity_link.setRel("self");
		identity_link.setHref(identityUrlV3.toExternalForm());
		metadata.getLinks().add(identity_link);
		MediaType jsonType = new MediaType();
		jsonType.setBase("application/json");
		jsonType.setType(MessageFormat.format(MEDIA_TYPE_JSON, V3));
		metadata.getMediaTypes().add(jsonType);
		MediaType xmlType = new MediaType();
		xmlType.setBase("application/xml");
		xmlType.setType(MessageFormat.format(MEDIA_TYPE_XML, V3));
		metadata.getMediaTypes().add(xmlType);
		return metadata;
	}

	public Response getVersions() throws MalformedURLException {
		List<Version> versions = getVersionList();
		VersionsWrapper versionsWrapper = new VersionsWrapper(new HashSet<Version>(versions));
		return Response.status(CustomResponseStatus.MULTIPLE_CHOICES).entity(versionsWrapper).build();
	}

	public VersionWrapper getVersionV3() throws MalformedURLException {
		List<Version> versions = getVersionList();
		for (Version version : versions) {
			if ("v3.0".equals(version.getId())) {
				return new VersionWrapper(version);
			}
		}
		throw Exceptions.VersionNotFoundException.getInstance(null, V3);
	}
}
