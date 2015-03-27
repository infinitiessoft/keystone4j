package com.infinities.keystone4j.ssl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

import net.oauth.signature.pem.PEMReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class X509CertificateParser {

	private final static Logger logger = LoggerFactory.getLogger(X509CertificateParser.class);


	private X509CertificateParser() {

	}

	public static X509Certificate parse(File f) throws CertificateException, IOException {
		InputStream inStream = null;
		InputStream is = null;
		logger.debug("cert file {} exist? {}", new Object[] { f.getAbsoluteFile(), f.exists() });
		try {
			inStream = new FileInputStream(f);
			PEMReader reader = new PEMReader(inStream);
			logger.debug("pem: {}, info: {}", new Object[] { f.getAbsoluteFile(), reader.getText() });
			is = new ByteArrayInputStream(reader.getDerBytes());
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
			return cert;
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error("close FileInputStream failed", e);
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("close ByteInputStream failed", e);
				}
			}
		}
	}

	public static PrivateKey parseKey(File f) throws CertificateException, IOException {
		FileReader fileReader = null;
		org.bouncycastle.openssl.PEMReader reader = null;
		logger.debug("cert file {} exist? {}", new Object[] { f.getAbsoluteFile(), f.exists() });
		try {
			fileReader = new FileReader(f);
			reader = new org.bouncycastle.openssl.PEMReader(fileReader);
			// logger.debug("pem: {}, info: {}", new Object[] {
			// f.getAbsoluteFile(), reader.getText() });
			Object obj = reader.readObject();
			if (obj instanceof KeyPair) {
				KeyPair keyPair = (KeyPair) obj;
				RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
				System.out.println("this is a keypair");
				return privateKey;
			} else if (obj instanceof PrivateKey) {
				RSAPrivateKey privateKey = (RSAPrivateKey) obj;
				System.out.println("this is a private key");
				return privateKey;
			} else {
				throw new RuntimeException("invalid key format");
			}
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
					logger.error("close FileInputStream failed", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("close FileInputStream failed", e);
				}
			}
		}
	}

}