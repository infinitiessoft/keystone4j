package com.infinities.keystone4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import org.glassfish.jersey.internal.util.Base64;

import com.infinities.keystone4j.common.Config;

public enum Cms {
	Instance;

	// private final static String CERT_FILE = "certfile";
	private final static String KEY_FILE = "keyfile";

	// private Certificate cert;
	private final Signature rsaSigner;


	private Cms() {
		// String certFile = Config.Instance.getOpt(Config.Type.signing,
		// CERT_FILE).asText();
		String keyFile = Config.Instance.getOpt(Config.Type.signing, KEY_FILE).asText();
		// FileInputStream fis = null;
		// BufferedInputStream bis = null;
		// CertificateFactory cf;
		// try {
		// fis = new FileInputStream(certFile);
		// bis = new BufferedInputStream(fis);
		// cf = CertificateFactory.getInstance("X.509");
		// while (bis.available() > 0) {
		// Certificate cert = cf.generateCertificate(bis);
		// }
		// } catch (Exception e) {
		// throw new RuntimeException("setup cerfile or keyfile fail", e);
		// } finally {
		// if (fis != null) {
		// try {
		// fis.close();
		// } catch (IOException e) {
		// // ignore
		// }
		// }
		// if (bis != null) {
		// try {
		// bis.close();
		// } catch (IOException e) {
		// // ignore
		// }
		// }
		// }

		File privateKeyFile = new File(keyFile);
		FileInputStream keyFis = null;
		try {
			keyFis = new FileInputStream(keyFile);
			byte[] encodedPrivateKey = new byte[(int) privateKeyFile.length()];
			keyFis.read(encodedPrivateKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			KeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			PrivateKey privateKey = kf.generatePrivate(keySpec);
			rsaSigner = Signature.getInstance("SHA1withRSA");
			rsaSigner.initSign(privateKey);
		} catch (Exception e) {
			throw new RuntimeException("setup cerfile or keyfile fail", e);
		} finally {
			if (keyFis != null) {
				try {
					keyFis.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

	}

	// certFile public_key & keyFile private_key
	public String signToken(String text) throws SignatureException, UnsupportedEncodingException {
		String output = signText(text);
		return toToken(output);
	}

	private String toToken(String output) {
		return output;
	}

	private String signText(String text) throws SignatureException, UnsupportedEncodingException {
		rsaSigner.update(text.getBytes("UTF8"));
		byte[] signaturesBytes = rsaSigner.sign();
		return Base64.encodeAsString(signaturesBytes);
	}

	public String hashToken(String tokenid) {
		return tokenid;
	}
}
