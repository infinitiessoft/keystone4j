/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import net.oauth.signature.pem.PEMReader;

import org.candlepin.thumbslug.ssl.PEMx509KeyManager;
import org.candlepin.thumbslug.ssl.SslPemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CutomSslContextFactory {

	private final static Logger logger = LoggerFactory.getLogger(CutomSslContextFactory.class);
	private static final String PROTOCOL = "TLS";


	private CutomSslContextFactory() {
		// for checkstyle
	}

	public static SSLContext getClientContext(String certUrl, String keyUrl) throws SslPemException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		logger.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");
		// TrustManager[] trustManagers = new TrustManager[] {
		// NO_OP_TRUST_MANAGER };
		X509Certificate cert;
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null);

		InputStream inputStream = new FileInputStream(new File(certUrl));
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
		} finally {
			inputStream.close();
		}
		String alias = cert.getSubjectX500Principal().getName();
		trustStore.setCertificateEntry(alias, cert);

		TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		tmf.init(trustStore);
		TrustManager[] trustManagers = tmf.getTrustManagers();

		return getClientContext(certUrl, keyUrl, trustManagers);
	}

	public static SSLContext getClientContext(String certUrl, String keyUrl, String caUrl) throws SslPemException {
		try {
			logger.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");
			// String caPem = Files.asCharSource(new File(caUrl),
			// Charsets.UTF_8).read();
			X509Certificate cert;
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null);

			InputStream inputStream = new FileInputStream(new File(caUrl));
			try {
				CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
				cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
			} finally {
				inputStream.close();
			}
			String alias = cert.getSubjectX500Principal().getName();
			trustStore.setCertificateEntry(alias, cert);

			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
			tmf.init(trustStore);
			TrustManager[] trustManagers = tmf.getTrustManagers();

			// Initialize the SSLContext to work with our key managers.
			return getClientContext(certUrl, keyUrl, trustManagers);
		} catch (Exception e) {
			logger.error("Unable to load pem file!", e);
			throw new SslPemException("Failed to initialize the client-side SSLContext", e);
		}
	}

	private static SSLContext getClientContext(String certUrl, String keyUrl, TrustManager[] trustManagerss)
			throws SslPemException {
		SSLContext clientContext = null;

		try {
			logger.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");

			PEMReader certReader = new PEMReader(certUrl);
			PEMReader keyReader = new PEMReader(keyUrl);

			String certificate = certReader.getText();
			String privateKey = keyReader.getText();
			logger.debug("certificate: {}", certificate);
			logger.debug("privateKey: {}", privateKey);

			PEMx509KeyManager[] managers = new PEMx509KeyManager[1];
			managers[0] = new PEMx509KeyManager();
			managers[0].addPEM(certificate, privateKey);

			// Initialize the SSLContext to work with our key managers.
			clientContext = SSLContext.getInstance(PROTOCOL);
			clientContext.init(managers, trustManagerss, null);
			logger.debug("SSL context initialized!");
		} catch (Exception e) {
			logger.error("Unable to load pem file!", e);
			throw new SslPemException("Failed to initialize the client-side SSLContext", e);
		}

		return clientContext;
	}


	private static TrustManager NO_OP_TRUST_MANAGER = new X509TrustManager() {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] certs, String authType) {
			return;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] certs, String authType) {
			return;
		}
	};

}
