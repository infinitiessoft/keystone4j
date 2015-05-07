/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Table;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.utils.FileScanner;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class FileScannerTest {

	private FileScanner scanner;


	@Before
	public void setUp() throws Exception {
		// URL url = getClass().getResource(KeystoneApplication.CONF_DIR +
		// "keystone.conf");
		// File file = new File(KeystoneApplication.CONF_DIR + "keystone.conf");
		// URL url = FileScannerTest.class.getResource("/keystone.conf");
		System.out.println(KeystoneUtils.getURL(KeystoneApplication.CONF_DIR + "keystone.conf"));

		scanner = new FileScanner(KeystoneUtils.getURL(KeystoneApplication.CONF_DIR + "keystone.conf"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRead() throws IOException {
		Table<Type, String, String> table = scanner.read();
		assertEquals("ADMIN2", table.get(Config.Type.DEFAULT, "admin_token"));
	}

}
