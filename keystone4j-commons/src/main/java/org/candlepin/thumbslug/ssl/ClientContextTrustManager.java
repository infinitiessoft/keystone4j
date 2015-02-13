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

/*
 * Adapted from Netty example code, which is
 * Copyright (C) 2008 Trustin Heuiseung Lee
 */

package org.candlepin.thumbslug.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * ClientContextTrustManager
 * 
 * The TrustManager Provided by this class is used to verify that the CDN we are
 * talking to as a client is a valid and recognized CDN. Thus, the
 * checkClientTrusted call will always fail.
 */
public class ClientContextTrustManager extends AbstractTrustManager {

	ClientContextTrustManager(X509Certificate[] cas) {
		this.cas = cas;

		// bypass this check if we were given null (for talking to candlepin)
		if (cas == null) {
			this.cas = new X509Certificate[0];
		}
	}

	public X509Certificate[] getAcceptedIssuers() {
		return cas;
	}

	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		throw new CertificateException("Using ClientContextTrustManager when acting as a server " + "- programmer error!");
	}

	public void checkServerTrusted(X509Certificate[] certs, String arg1) throws CertificateException {
		checkCertificateChain(certs);
	}

	public static X509TrustManager[] getTrustManagers(X509Certificate[] cas) {
		return new X509TrustManager[] { new ClientContextTrustManager(cas) };
	}
}
