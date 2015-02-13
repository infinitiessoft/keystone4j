package com.infinities.keystone4j.ssl;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateVerifier {

	private final static Logger logger = LoggerFactory.getLogger(CertificateVerifier.class);


	private CertificateVerifier() {

	}

	// public static boolean validateKeyChain(X509Certificate client,
	// X509Certificate... trustedCerts)
	// throws CertificateException, InvalidAlgorithmParameterException,
	// NoSuchAlgorithmException,
	// NoSuchProviderException {
	// boolean found = false;
	// int i = trustedCerts.length;
	// CertificateFactory cf = CertificateFactory.getInstance("X.509");
	// TrustAnchor anchor;
	// Set<TrustAnchor> anchors;
	// CertPath path;
	// List<Certificate> list;
	// PKIXParameters params;
	// CertPathValidator validator = CertPathValidator.getInstance("PKIX");
	//
	// while (!found && i > 0) {
	//
	// anchor = new TrustAnchor(trustedCerts[--i], null);
	// anchors = Collections.singleton(anchor);
	// list = Arrays.asList(new Certificate[] { client });
	// path = cf.generateCertPath(list);
	// params = new PKIXParameters(anchors);
	// params.setRevocationEnabled(false);
	//
	// if (client.getIssuerDN().equals(trustedCerts[i].getSubjectDN())) {
	// try {
	// validator.validate(path, params);
	// if (isSelfSigned(trustedCerts[i])) {
	// // found root ca
	// found = true;
	// logger.info("validating root {}",
	// trustedCerts[i].getSubjectX500Principal().getName());
	//
	// } else if (!client.equals(trustedCerts[i])) {
	// // find parent ca
	// logger.info("validating via: {}",
	// trustedCerts[i].getSubjectX500Principal().getName());
	// found = validateKeyChain(trustedCerts[i], trustedCerts);
	// }
	//
	// } catch (CertPathValidatorException e) {
	// // validation fail, check next certifiacet in the
	// // trustedCerts array
	// }
	// }
	// }
	// return found;
	// }

	/**
	 * Checks whether given X.509 certificate is self-signed.
	 */
	public static boolean isSelfSigned(X509Certificate cert) throws CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException {
		try {
			// Try to verify certificate signature with its own public key
			PublicKey key = cert.getPublicKey();
			cert.verify(key);
			logger.debug("certificate is self-signer cert");
			return true;
		} catch (SignatureException sigEx) {
			logger.debug("not self-signer cert", sigEx);
			// Invalid signature --> not self-signed
			return false;
		} catch (InvalidKeyException keyEx) {
			logger.debug("not self-signer cert", keyEx);
			// Invalid key --> not self-signed
			return false;
		}
	}

	public static PKIXCertPathBuilderResult verifyCertificate(X509Certificate cert, Set<X509Certificate> additionalCerts,
			boolean verifySelfSignedCert) throws CertificateVerificationException {
		try {
			// Check for self-signed certificate
			if (!verifySelfSignedCert && isSelfSigned(cert)) {
				throw new CertificateVerificationException("The certificate is self-signed.");
			}

			// Prepare a set of trusted root CA certificates
			// and a set of intermediate certificates
			Set<X509Certificate> trustedRootCerts = new HashSet<X509Certificate>();
			Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
			for (X509Certificate additionalCert : additionalCerts) {
				if (isSelfSigned(additionalCert)) {
					trustedRootCerts.add(additionalCert);
				} else {
					intermediateCerts.add(additionalCert);
				}
			}

			// Attempt to build the certification chain and verify it
			PKIXCertPathBuilderResult verifiedCertChain = verifyCertificate(cert, trustedRootCerts, intermediateCerts,
					verifySelfSignedCert);

			// Check whether the certificate is revoked by the CRL
			// given in its CRL distribution point extension
			CRLVerifier.verifyCertificateCRLs(cert);

			// The chain is built and verified. Return it as a result
			return verifiedCertChain;
		} catch (CertPathBuilderException certPathEx) {
			throw new CertificateVerificationException("Error building certification path: "
					+ cert.getSubjectX500Principal(), certPathEx);
		} catch (CertificateVerificationException cvex) {
			throw cvex;
		} catch (Exception ex) {
			throw new CertificateVerificationException("Error verifying the certificate: " + cert.getSubjectX500Principal(),
					ex);
		}
	}

	private static PKIXCertPathBuilderResult verifyCertificate(X509Certificate cert, Set<X509Certificate> trustedRootCerts,
			Set<X509Certificate> intermediateCerts, boolean verifySelfSignedCert) throws GeneralSecurityException {

		// Create the selector that specifies the starting certificate
		X509CertSelector selector = new X509CertSelector();
		selector.setCertificate(cert);

		// Create the trust anchors (set of root CA certificates)
		Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
		for (X509Certificate trustedRootCert : trustedRootCerts) {
			trustAnchors.add(new TrustAnchor(trustedRootCert, null));
		}

		// Configure the PKIX certificate builder algorithm parameters
		PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(trustAnchors, selector);

		// Disable CRL checks (this is done manually as additional step)
		pkixParams.setRevocationEnabled(false);

		// Specify a list of intermediate certificates
		CertStore intermediateCertStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(
				intermediateCerts));
		pkixParams.addCertStore(intermediateCertStore);

		// Build and verify the certification chain
		CertPathBuilder builder = CertPathBuilder.getInstance("PKIX");
		PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder.build(pkixParams);
		return result;
	}

}
