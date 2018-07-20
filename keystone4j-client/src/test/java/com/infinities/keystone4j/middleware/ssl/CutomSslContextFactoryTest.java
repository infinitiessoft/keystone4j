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

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.candlepin.thumbslug.ssl.SslPemException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.ssl.CutomSslContextFactory;

public class CutomSslContextFactoryTest {

	private final String cert = Thread.currentThread().getContextClassLoader().getResource("signing_cert.pem").getPath();
	private final String key = Thread.currentThread().getContextClassLoader().getResource("signing_key.pem").getPath();
	private final String ca = Thread.currentThread().getContextClassLoader().getResource("cacert.pem").getPath();


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetClientContextStringString() throws SslPemException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		CutomSslContextFactory.getClientContext(cert, key);
	}

	@Test
	public void testGetClientContextStringStringString() throws SslPemException {
		CutomSslContextFactory.getClientContext(cert, key, ca);
	}

}
