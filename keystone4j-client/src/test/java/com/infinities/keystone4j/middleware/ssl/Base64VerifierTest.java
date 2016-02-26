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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.BaseEncoding;
import com.infinities.keystone4j.ssl.Base64Verifier;

public class Base64VerifierTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsBase64Byte() {
		byte a = 'a';
		byte b = '#';
		assertFalse(Base64Verifier.isBase64(b));
		assertTrue(Base64Verifier.isBase64(a));
	}

	@Test
	public void testIsBase64String() {
		String text = "test!@#$%^&*()_+~";
		assertFalse(Base64Verifier.isBase64(text));
		assertTrue(Base64Verifier.isBase64(BaseEncoding.base64().encode(text.getBytes())));
	}

	@Test
	public void testIsBase64ByteArray() {
		String text = "test!@#$%^&*()_+~";
		byte[] b = text.getBytes();
		assertFalse(Base64Verifier.isBase64(b));
		assertTrue(Base64Verifier.isBase64(BaseEncoding.base64().encode(b).getBytes()));
	}

}
