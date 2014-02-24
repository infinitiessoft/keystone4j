package com.infinities.keystone4j.common.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.exception.VersionNotFoundException;

public class VersionApi {

	private final static String DEFAULT_VERSION = "v2.0";
	private final static String MEDIA_TYPE_JSON = "application/vnd.openstack.identity-{0}+json";
	private final static String MEDIA_TYPE_XML = "application/vnd.openstack.identity-{0}+xml";
	private final static String V3 = "v3";
	private String type;
	private Properties properties;
	private final static String URL_POSTFIX = "{0}_endpoint";
	private final static String PORT_POSTFIX = "{0}_port";
	private List<String> versions = Lists.newArrayList();


	public VersionApi(Properties properties, String type) {
		this.setType(type);
		this.properties = properties;
	}

	protected URL getIdentityURL(String version) throws MalformedURLException {
		String urlKey = MessageFormat.format(URL_POSTFIX, type);
		String url = properties.getProperty(urlKey);

		String portKey = MessageFormat.format(PORT_POSTFIX, type);
		String port = properties.getProperty(portKey);
		url = MessageFormat.format(url, port);

		if (!url.endsWith("/")) {
			url += "/";
		}

		if (Strings.isNullOrEmpty(version)) {
			version = VersionApi.DEFAULT_VERSION;
		}

		return new URL(url + version + "/");
	}

	protected List<Version> getVersionList() throws MalformedURLException {
		List<Version> versionMetadatas = Lists.newArrayList();
		if (versions.contains(DEFAULT_VERSION)) {
			versionMetadatas.add(newV2Metadata());
		}
		if (versions.contains(V3)) {
			versionMetadatas.add(newV3Metadata());
		}

		return versionMetadatas;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private Version newV2Metadata() throws MalformedURLException {
		Version metadata = new Version();
		metadata.setId("v2.0");
		metadata.setStatus("stable");
		metadata.setUpdated("2013-03-06T00:00:00Z");
		URL identityUrlV2 = getIdentityURL(DEFAULT_VERSION);
		Link identity_link = new Link();
		identity_link.setRel("self");
		identity_link.setHref(identityUrlV2.toExternalForm());
		metadata.getLinks().add(identity_link);
		Link desc_link = new Link();
		desc_link.setRel("describedby");
		desc_link.setType("text/html");
		desc_link.setHref("http://docs.openstack.org/api/openstack-identity-service/2.0/content/");
		metadata.getLinks().add(desc_link);
		Link guide_link = new Link();
		guide_link.setRel("describedby");
		guide_link.setType("application/pdf");
		guide_link.setHref("http://docs.openstack.org/api/openstack-identity-service/2.0/identity-dev-guide-2.0.pdf");
		metadata.getLinks().add(guide_link);
		MediaType jsonType = new MediaType();
		jsonType.setBase("application/json");
		jsonType.setType(MessageFormat.format(MEDIA_TYPE_JSON, "v2.0"));
		metadata.getMediaTypes().add(jsonType);
		MediaType xmlType = new MediaType();
		xmlType.setBase("application/xml");
		xmlType.setType(MessageFormat.format(MEDIA_TYPE_XML, "v2.0"));
		metadata.getMediaTypes().add(xmlType);
		return metadata;
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

	public VersionWrapper getVersionV2() throws MalformedURLException {
		List<Version> versions = getVersionList();
		for (Version version : versions) {
			if (DEFAULT_VERSION.equals(version.getId())) {
				return new VersionWrapper(version);
			}
		}
		throw new VersionNotFoundException(null, DEFAULT_VERSION);
	}

	public VersionWrapper getVersionV3() throws MalformedURLException {
		List<Version> versions = getVersionList();
		for (Version version : versions) {
			if (V3.equals(version.getId())) {
				return new VersionWrapper(version);
			}
		}
		throw new VersionNotFoundException(null, V3);
	}
}
