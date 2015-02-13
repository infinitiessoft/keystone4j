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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;

/**
 * ServerContextTrustManagerFactory
 * 
 * The TrustManager Provided by this class is used to verify that clients
 * connecting to thumbslug are valid clients. As such, you'll notice that the
 * checkServerTrusted call always fails. we don't want to use this trust manager
 * when acting as a client to someone else!
 */
public class ServerContextTrustManager extends AbstractTrustManager {

	ServerContextTrustManager(X509Certificate[] cas) {
		this.cas = cas;
	}

	public X509Certificate[] getAcceptedIssuers() {
		return cas;
	}

	public void checkClientTrusted(X509Certificate[] certs, String arg1) throws CertificateException {
		checkCertificateChain(certs);
		// We don't need to check if the client certificate has been revoked or
		// not here. That's handled later on by checking if candlepin knows
		// about
		// it.
	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		throw new CertificateException("Using ServerContextTrustManager when acting as a client " + "- programmer error!");
	}

	static TrustManager[] getTrustManagers(X509Certificate[] cas) {
		return new TrustManager[] { new ServerContextTrustManager(cas) };
	}
}
