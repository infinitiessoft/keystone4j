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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import net.oauth.signature.pem.PEMReader;

/**
 * PEMChainLoader
 * 
 * Read a PEM file full of 1 or more public CA certificates, and create a
 * certificate chain for authenticating a peer's certificate.
 */
public class PemChainLoader {

	private PemChainLoader() {
		// for checkstyle
	}

	public static X509Certificate[] loadChain(String pem) throws SslPemException {
		if (pem == null || pem.equals("")) {
			throw new SslPemException("Empty pem");
		}

		List<String> parts = splitPem(pem);

		X509Certificate[] chain = new X509Certificate[parts.size()];
		try {
			int i = 0;
			for (String part : parts) {
				chain[i] = getX509CertificateFromPem(part);
				i++;
			}
		} catch (IOException e) {
			throw new SslPemException("Unable to parse pem", e);
		} catch (GeneralSecurityException e) {
			throw new SslPemException("Unable to parse pem bytes", e);
		}

		return chain;
	}

	private static List<String> splitPem(String pem) {
		// unfortunately the PEMReader class doesn't seem to let you read
		// multiple
		// sections in one go, so we have to manually split.

		int index = 0;
		List<String> pems = new ArrayList<String>();
		while (index < pem.length()) {
			int newIndex = pem.indexOf(PEMReader.CERTIFICATE_X509_MARKER, index + 1);
			if (newIndex == -1) {
				newIndex = pem.length();
			}
			pems.add(pem.substring(index, newIndex));

			index = newIndex;
		}

		return pems;
	}

	private static X509Certificate getX509CertificateFromPem(String pem) throws GeneralSecurityException, IOException {

		InputStream stream = new ByteArrayInputStream(pem.getBytes("UTF-8"));

		PEMReader reader = new PEMReader(stream);
		byte[] bytes = reader.getDerBytes();

		if (!PEMReader.CERTIFICATE_X509_MARKER.equals(reader.getBeginMarker())) {
			throw new IOException("Invalid PEM file: Unknown marker for " + " public key or cert " + reader.getBeginMarker());
		}

		CertificateFactory fac = CertificateFactory.getInstance("X509");
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		X509Certificate cert = (X509Certificate) fac.generateCertificate(in);

		return cert;
	}

}
