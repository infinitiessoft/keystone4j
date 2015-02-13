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

import javax.net.ssl.X509TrustManager;

/**
 * AbstractTrustManager
 */
public abstract class AbstractTrustManager implements X509TrustManager {

	protected X509Certificate[] cas;


	protected void checkCertificateChain(X509Certificate[] certs) throws CertificateException {
		// work up the client certs chain, verifying the current against the
		// next.
		// once we hit the end, see if it verifies against _any_ of the certs in
		// the ca
		// chain.
		if (certs.length > 1) {
			for (int i = 1; i < certs.length; i++) {
				try {
					certs[i - 1].verify(certs[i].getPublicKey());
				} catch (Exception e) {
					// Any exception is a failure to verify.
					throw new CertificateException(e);
				}
			}
		}

		certs[certs.length - 1].checkValidity();

		// if no ca certs, like for talking to candlepin, jump out early
		if (cas.length == 0) {
			return;
		}

		for (X509Certificate ca : cas) {
			try {
				certs[certs.length - 1].verify(ca.getPublicKey());
				// if we don't get an exception, it has verified.
				return;
			} catch (Exception e) {
				// do nothing
			}
		}

		// we have exhausted all certificates. give up
		throw new CertificateException("Unable to verify server chain");
	}
}
