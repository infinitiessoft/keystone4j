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

package com.infinities.keystone4j.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.zip.DataFormatException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompressionUtilsTest {

	private final String token = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678910";


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompress() {
		byte[] ret = CompressionUtils.compress(token.getBytes());
		assertTrue(ret.length > token.getBytes().length);
	}

	@Test
	public void testDecompress() throws DataFormatException {
		byte[] ret = CompressionUtils.compress(token.getBytes());
		byte[] ret2 = CompressionUtils.decompress(ret);
		String str = new String(ret2);
		assertEquals(token, str);
	}

}
