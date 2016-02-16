/**
 * Copyright (c) 2011 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.thumbslug.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SslContextFactory
 */
public class SslContextFactory {

	private static Logger log = LoggerFactory.getLogger(SslContextFactory.class);

	private static final String PROTOCOL = "TLS";

	private static KeyStore ks;
	private static X509Certificate[] chain;


	private SslContextFactory() {
		// for checkstyle
	}

	public static SSLContext getServerContext(String keystoreUrl, String keystorePassword, String caUrl)
			throws SslKeystoreException, SslPemException {

		SSLContext serverContext;

		String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
		if (algorithm == null) {
			algorithm = "SunX509";
		}

		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			if (ks == null) {
				log.info("reading keystore");
				fis = new FileInputStream(new File(keystoreUrl));
				ks = KeyStore.getInstance("PKCS12");
				ks.load(fis, keystorePassword.toCharArray());

				scanner = new Scanner(new File(caUrl));
				scanner.useDelimiter("\\Z");
				String caPem = scanner.next();
				chain = PemChainLoader.loadChain(caPem);
			}

			// Set up key manager factory to use our key store
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
			kmf.init(ks, keystorePassword.toCharArray());

			// Initialize the SSLContext to work with our key managers.
			serverContext = SSLContext.getInstance(PROTOCOL);
			serverContext.init(kmf.getKeyManagers(), ServerContextTrustManager.getTrustManagers(chain), null);
		} catch (SslPemException e) {
			throw e;
		} catch (Exception e) {
			throw new SslKeystoreException("Failed to initialize the server-side SSLContext.", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new Error("Failed to initialize the client-side SSLContext", e);
				}

			}
			if (scanner != null) {
				scanner.close();
			}
		}

		return serverContext;
	}

	public static SSLContext getClientContext(String pem, String caUrl) throws SslPemException {
		SSLContext clientContext = null;
		Scanner scanner = null;

		try {
			log.debug("loading thumbslug to cdn entitlement certificate (pem encoded)");

			CertParser parser = new CertParser(pem);
			String certificate = parser.getCert();
			String privateKey = parser.getKey();

			PEMx509KeyManager[] managers = new PEMx509KeyManager[1];
			managers[0] = new PEMx509KeyManager();
			managers[0].addPEM(certificate, privateKey);

			scanner = new Scanner(new File(caUrl));
			scanner.useDelimiter("\\Z");
			String caPem = scanner.next();

			// Initialize the SSLContext to work with our key managers.
			clientContext = SSLContext.getInstance(PROTOCOL);
			clientContext.init(managers, ClientContextTrustManager.getTrustManagers(PemChainLoader.loadChain(caPem)), null);
			log.debug("SSL context initialized!");
		} catch (Exception e) {
			log.error("Unable to load pem file!", e);
			throw new SslPemException("Failed to initialize the client-side SSLContext", e);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return clientContext;
	}

	public static SSLContext getCandlepinClientContext() {
		// Candlepin client context, we won't be sending up an ssl cert,
		// just verifying that of candlepin

		SSLContext candlepinClientContext;

		try {
			candlepinClientContext = SSLContext.getInstance(PROTOCOL);
			candlepinClientContext.init(null, ClientContextTrustManager.getTrustManagers(null), null);
		} catch (NoSuchAlgorithmException e) {
			throw new Error("Failed to initialize the thumbslug to candlepin ssl context", e);
		} catch (KeyManagementException e) {
			throw new Error("Failed to initialize the thumbslug to candlepin ssl context", e);
		}

		return candlepinClientContext;
	}
}
