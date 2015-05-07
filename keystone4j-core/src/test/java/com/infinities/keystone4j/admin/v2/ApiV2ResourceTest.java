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
package com.infinities.keystone4j.admin.v2;

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

public class ApiV2ResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ApiV2ResourceTestApplication();
	}

	@Test
	public void testGetVersions() throws JsonParseException, IOException {
		Response response = target("/").path("v2.0").request().get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode versionV2 = node.get("version");
		assertNotNull(versionV2);
		assertEquals("v2.0", versionV2.get("id").asText());
		assertEquals("stable", versionV2.get("status").asText());
		assertEquals("2014-04-17T00:00:00Z", versionV2.get("updated").asText());
		JsonNode linksV2 = versionV2.get("links");
		assertEquals(3, linksV2.size());
		JsonNode linkV2_0 = linksV2.get(0);
		assertEquals("self", linkV2_0.get("rel").asText());
		String url = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_endpoint").asText();
		url = Config.replaceVarWithConf(url) + "v2.0/";
		assertEquals(url, linkV2_0.get("href").asText());
		JsonNode linkV2_1 = linksV2.get(1);
		assertEquals("describeby", linkV2_1.get("rel").asText());
		assertEquals("text/html", linkV2_1.get("type").asText());
		assertEquals("http://docs.openstack.org/api/openstack-identity-service/v2.0/content/", linkV2_1.get("href").asText());
		JsonNode linkV2_2 = linksV2.get(2);
		assertEquals("describeby", linkV2_2.get("rel").asText());
		assertEquals("application/pdf", linkV2_2.get("type").asText());
		assertEquals("http://docs.openstack.org/api/openstack-identity-service/2.0/identity-dev-guide-2.0.pdf", linkV2_2
				.get("href").asText());
		JsonNode mediasV2 = versionV2.get("media-types");
		assertEquals(1, mediasV2.size());
		JsonNode mediaV2 = mediasV2.get(0);
		assertEquals("application/json", mediaV2.get("base").asText());
		assertEquals("application/vnd.openstack.identity-v2.0+json", mediaV2.get("type").asText());
	}
}
