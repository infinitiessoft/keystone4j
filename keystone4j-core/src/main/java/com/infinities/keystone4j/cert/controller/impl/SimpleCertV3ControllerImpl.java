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
package com.infinities.keystone4j.cert.controller.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response;

import com.google.common.io.Files;
import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.extension.ExtensionApi;
import com.infinities.keystone4j.extension.model.Extension;
import com.infinities.keystone4j.model.common.Link;
import com.infinities.keystone4j.utils.KeystoneUtils;

//keystone.contrib.simple_cert.core 20141129
//keystone.contrib.simple_cert.controllers 20141129

public class SimpleCertV3ControllerImpl extends BaseController implements SimpleCertV3Controller {

	// private final static Logger logger =
	// LoggerFactory.getLogger(SimpleCertV3ControllerImpl.class);

	public SimpleCertV3ControllerImpl(ExtensionApi extensionApi) {
		Extension extension = new Extension();
		extension.setName("Openstack Simple Certificate API");
		extension.setNamespace("http://docs.openstack.org/identity/api/ext/OS-SIMPLE-CERT/v1.0");
		extension.setAlias("OS-SIMPLE-CERT");
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 1, 20, 12, 0);
		Date updated = calendar.getTime();
		extension.setUpdated(updated);
		extension.setDescription("Openstack simple certificate retrieval extension");
		Link link = new Link();
		link.setRel("describeby");
		link.setType("text/html");
		link.setHref("https://github.com/openstack/identity-api");
		extension.getLinks().add(link);
		extensionApi.registerExtension(extension.getAlias(), extension);
	}

	@Override
	public Response getCaCertificate() {
		return getCertificate(Config.getOpt(Config.Type.signing, "ca_certs").asText());
	}

	@Override
	public Response listCertificate() {
		return getCertificate(Config.getOpt(Config.Type.signing, "certfile").asText());
	}

	private Response getCertificate(String text) {
		try {
			// URL url = getClass().getResource(text);
			// File file = new File(text);
			URL url = KeystoneUtils.getURL(text);
			return Response.status(200).type("application/x-pem-file").entity(getBytesFromFile(url)).build();
		} catch (Exception e) {
			throw Exceptions.CertificateFilesUnavailableException.getInstance(null);
		}
	}

	private byte[] getBytesFromFile(URL url) throws IOException {
		File file = new File(url.getPath());
		return Files.toByteArray(file);

		// InputStream is = null;
		// try {
		// File file = new File(url.getPath());
		// is = new FileInputStream(file);
		//
		// long length = file.length();
		//
		// if (length > Integer.MAX_VALUE) {
		// logger.warn("File: {} is too large", file.getName());
		// throw new RuntimeException("file is too large " + file.getName());
		// }
		//
		// byte[] bytes = new byte[(int) length];
		// int offset = 0;
		// int numRead = 0;
		//
		// while (offset < bytes.length && (numRead = is.read(bytes, offset,
		// bytes.length - offset)) >= 0) {
		// offset += numRead;
		// }
		//
		// if (offset < bytes.length) {
		// throw new IOException("Could not completely read file " +
		// file.getName());
		// }
		//
		// return bytes;
		// } finally {
		// is.close();
		// }
	}
}
