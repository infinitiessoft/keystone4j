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
package com.infinities.keystone4j.admin.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.utils.JsonUtils;

public class ApiV3ResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ApiV3ResourceTestApplication();
	}

	@Test
	public void testGetVersion() throws JsonParseException, IOException {
		Response response = target("/").path("v3").request().get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode versionV3 = node.get("version");
		assertNotNull(versionV3);
		assertEquals("v3.0", versionV3.get("id").asText());
		assertEquals("stable", versionV3.get("status").asText());
		assertEquals("2013-03-06T00:00:00Z", versionV3.get("updated").asText());
		JsonNode links = versionV3.get("links");
		assertEquals(1, links.size());
		JsonNode link = links.get(0);
		assertEquals("self", link.get("rel").asText());
		String url = Config.getOpt(Config.Type.DEFAULT, "admin_endpoint").asText();
		url = Config.replaceVarWithConf(url) + "v3/";
		assertEquals(url, link.get("href").asText());
		JsonNode medias = versionV3.get("media-types");
		assertEquals(1, medias.size());
		JsonNode media = medias.get(0);
		assertEquals("application/json", media.get("base").asText());
		assertEquals("application/vnd.openstack.identity-v3+json", media.get("type").asText());
	}
}
