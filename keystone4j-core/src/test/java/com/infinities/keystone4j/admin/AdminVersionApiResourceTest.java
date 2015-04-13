package com.infinities.keystone4j.admin;

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

public class AdminVersionApiResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new AdminVersionApiResourceTestApplication();
	}

	@Test
	public void testGetVersions() throws JsonParseException, IOException {
		Response response = target("/").request().get();
		assertEquals(300, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode versions = node.get("versions");
		assertNotNull(versions);
		JsonNode values = versions.get("values");
		assertEquals(2, values.size());
		JsonNode versionV2 = values.get(0);
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

		JsonNode versionV3 = values.get(1);
		assertNotNull(versionV3);
		assertEquals("v3.0", versionV3.get("id").asText());
		assertEquals("stable", versionV3.get("status").asText());
		assertEquals("2013-03-06T00:00:00Z", versionV3.get("updated").asText());
		JsonNode links = versionV3.get("links");
		assertEquals(1, links.size());
		JsonNode link = links.get(0);
		assertEquals("self", link.get("rel").asText());
		url = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_endpoint").asText();
		url = Config.replaceVarWithConf(url) + "v3/";
		assertEquals(url, link.get("href").asText());
		JsonNode medias = versionV3.get("media-types");
		assertEquals(1, medias.size());
		JsonNode media = medias.get(0);
		assertEquals("application/json", media.get("base").asText());
		assertEquals("application/vnd.openstack.identity-v3+json", media.get("type").asText());
	}
}
