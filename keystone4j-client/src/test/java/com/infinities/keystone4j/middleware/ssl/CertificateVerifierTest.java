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

package com.infinities.keystone4j.middleware.ssl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.ssl.CertificateVerificationException;
import com.infinities.keystone4j.ssl.CertificateVerifier;
import com.infinities.keystone4j.ssl.X509CertificateParser;

public class CertificateVerifierTest {

	private X509Certificate signercert;
	private X509Certificate cacert;


	@Before
	public void setUp() throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		URL signingCertUrl = Thread.currentThread().getContextClassLoader().getResource("signing_cert.pem");
		URL cacertUrl = Thread.currentThread().getContextClassLoader().getResource("cacert.pem");

		signercert = X509CertificateParser.parse(new File(signingCertUrl.getPath()));
		cacert = X509CertificateParser.parse(new File(cacertUrl.getPath()));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testValidateKeyChain() throws CertificateException, InvalidAlgorithmParameterException,
			NoSuchAlgorithmException, NoSuchProviderException, CertificateVerificationException {
		// assertTrue(CertificateVerifier.validateKeyChain(signercert, cacert));

		Set<X509Certificate> additions = new HashSet<X509Certificate>();
		additions.add(cacert);
		CertificateVerifier.verifyCertificate(signercert, additions, true);
	}

	@Test
	public void testIsSelfSigned() throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException {
		assertFalse(CertificateVerifier.isSelfSigned(signercert));
		assertTrue(CertificateVerifier.isSelfSigned(cacert));
	}

}
