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
package com.infinities.keystone4j.common;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.option.Option;

public class ConfigTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetOpt() {
		Option adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token");
		assertEquals("ADMIN2", adminToken.asText());
	}

	@Test
	public void testGetOpt_subpattern() {
		Option endpoint = Config.Instance.getOpt(Config.Type.DEFAULT, "public_endpoint");
		assertEquals("http://localhost:9999/", endpoint.asText());
	}

}
