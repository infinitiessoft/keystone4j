package com.infinities.keystone4j.ssl;

import java.io.File;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.oauth.signature.pem.PEMReader;

import org.candlepin.thumbslug.ssl.ClientContextTrustManager;
import org.candlepin.thumbslug.ssl.PEMx509KeyManager;
import org.candlepin.thumbslug.ssl.PemChainLoader;
import org.candlepin.thumbslug.ssl.SslPemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class CustomSslContextFactory {

	private final static Logger logger = LoggerFactory.getLogger(CustomSslContextFactory.class);
	private static final String PROTOCOL = "TLS";


	private CustomSslContextFactory() {
		// for checkstyle
	}

	public static SSLContext getClientContext(String certUrl, String keyUrl) throws SslPemException {
		logger.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");
		TrustManager[] trustManagers = new TrustManager[] { NO_OP_TRUST_MANAGER };
		return getClientContext(certUrl, keyUrl, trustManagers);
	}

	public static SSLContext getClientContext(String certUrl, String keyUrl, String caUrl) throws SslPemException {
		try {
			logger.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");
			String caPem = Files.asCharSource(new File(caUrl), Charsets.UTF_8).read();
			// Initialize the SSLContext to work with our key managers.
			return getClientContext(certUrl, keyUrl,
					ClientContextTrustManager.getTrustManagers(PemChainLoader.loadChain(caPem)));
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
