package com.infinities.keystone4j.cert.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.cert.controller.SimpleCertV3Controller;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.model.Link;
import com.infinities.keystone4j.exception.CertificateFilesUnavailableException;
import com.infinities.keystone4j.extension.ExtensionApi;
import com.infinities.keystone4j.extension.model.Extension;

public class SimpleCertV3ControllerImpl implements SimpleCertV3Controller {

	private final static Logger logger = LoggerFactory.getLogger(SimpleCertV3ControllerImpl.class);


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
		return getCertificate(Config.Instance.getOpt(Config.Type.signing, "ca_certs").asText());
	}

	@Override
	public Response listCertificate() {
		return getCertificate(Config.Instance.getOpt(Config.Type.signing, "certfile").asText());
	}

	private Response getCertificate(String text) {
		try {
			File file = new File(text);
			return Response.status(200).type("application/x-pem-file").entity(getBytesFromFile(file)).build();
		} catch (Exception e) {
			throw new CertificateFilesUnavailableException(null);
		}
	}

	private byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(file);

			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				logger.warn("File: {} is too large", file.getName());
				throw new RuntimeException("file is too large " + file.getName());
			}

			byte[] bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;

			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}

			return bytes;
		} finally {
			is.close();
		}
	}
}
