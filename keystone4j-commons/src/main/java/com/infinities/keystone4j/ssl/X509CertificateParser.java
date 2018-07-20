/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
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

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
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
		PEMParser reader = null;
		logger.debug("cert file {} exist? {}", new Object[] { f.getAbsoluteFile(), f.exists() });
		try {
			fileReader = new FileReader(f);
			reader = new PEMParser(fileReader);
			// logger.debug("pem: {}, info: {}", new Object[] {
			// f.getAbsoluteFile(), reader.getText() });
			Object obj = reader.readObject();
			if (obj instanceof KeyPair) {
				KeyPair keyPair = (KeyPair) obj;
				RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
				logger.debug("this is a keypair");
				return privateKey;
			} else if (obj instanceof PrivateKey) {
				RSAPrivateKey privateKey = (RSAPrivateKey) obj;
				logger.debug("this is a private key");
				return privateKey;
			} else if (obj instanceof PEMKeyPair) {
				JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
				KeyPair keypair = converter.getKeyPair((PEMKeyPair) obj);
				return keypair.getPrivate();
			} else if (obj instanceof PrivateKeyInfo) {
				JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
				return converter.getPrivateKey(PrivateKeyInfo.getInstance(obj));
			} else {

				logger.debug("obj class:{}", obj.getClass());
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
