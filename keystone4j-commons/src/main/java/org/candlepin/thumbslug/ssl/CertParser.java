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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* CertParser: Parses the certificate and key out of the given PEM.
*
* Knows to ignore extra entitlement data and signatures for V3 certificates.
*/
public class CertParser {

    private static Logger log = LoggerFactory.getLogger(SslContextFactory.class);

    private String cert;
    private String key;

    private static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    private static final String END_CERT = "-----END CERTIFICATE-----";
    private static final String BEGIN_KEY_PKCS1 = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String END_KEY_PKCS1 = "-----END RSA PRIVATE KEY-----";
    private static final String BEGIN_KEY_PKCS8 = "-----BEGIN PRIVATE KEY-----";
    private static final String END_KEY_PKCS8 = "-----END PRIVATE KEY-----";

    public CertParser(String pem) {
        // split the pem into its two parts, then figure out which is
        // the private and which is the public part
        log.debug("Cert is: \n" + pem);
        int certBegin = pem.indexOf(BEGIN_CERT);
        int certEnd = pem.indexOf(END_CERT);

        if ((certBegin == -1) || (certEnd == -1)) {
            throw new IllegalArgumentException("unable to parse PEM data");
        }

        // Try to find PKCS1 key marker first:
        int keyBegin = pem.indexOf(BEGIN_KEY_PKCS1);
        int keyEnd = pem.indexOf(END_KEY_PKCS1);
        String keyEndMarker = END_KEY_PKCS1; // for the end key length offset later

        // If none found, try PKCS8 key marker:
        if ((keyBegin == -1) || (keyEnd == -1)) {
            keyBegin = pem.indexOf(BEGIN_KEY_PKCS8);
            keyEnd = pem.indexOf(END_KEY_PKCS8);
            keyEndMarker = END_KEY_PKCS8;
        }

        if ((keyBegin == -1) || (keyEnd == -1)) {
            throw new IllegalArgumentException("unable to parse PEM data");
        }

        // Expand to the actual end of the strings:
        certEnd = certEnd + END_CERT.length();
        keyEnd = keyEnd + keyEndMarker.length();

        this.cert = pem.substring(certBegin, certEnd);
        this.key = pem.substring(keyBegin, keyEnd);
    }

    public String getCert() {
        return this.cert;
    }

    public String getKey() {
        return this.key;
    }

}
