package com.infinities.keystone4j.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.TestingApplication;

public class PublicResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new TestingApplication();
	}

	@Test
	public void testGetApiV3Resource() throws JsonParseException, IOException {
		Response response = target("/").request().get();
		assertEquals(300, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode versions = node.get("versions");
		assertNotNull(versions);
		assertEquals(1, versions.size());
		JsonNode version = versions.get(0);
		assertNotNull(version);
		assertEquals("v3.0", version.get("id").asText());
		assertEquals("stable", version.get("status").asText());
		assertEquals("2013-03-06T00:00:00Z", version.get("updated").asText());
		JsonNode links = version.get("links");
		assertEquals(1, links.size());
		JsonNode link = links.get(0);
		assertEquals("self", link.get("rel").asText());
		assertEquals("http://localhost:5000/v3/", link.get("href").asText());
		JsonNode medias = version.get("media-types");
		assertEquals(2, medias.size());
		for (JsonNode n : medias) {
			assertNotNull(n.get("base"));
			assertNotNull(n.get("type"));
		}
	}

	@Test
	public void testGetPublicVersionApiResource() throws JsonProcessingException, IOException {
		Response response = target("/v3").request().get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode version = node.get("version");
		assertNotNull(version);
		assertEquals("v3.0", version.get("id").asText());
		assertEquals("stable", version.get("status").asText());
		assertEquals("2013-03-06T00:00:00Z", version.get("updated").asText());
		JsonNode links = version.get("links");
		assertEquals(1, links.size());
		JsonNode link = links.get(0);
		assertEquals("self", link.get("rel").asText());
		assertEquals("http://localhost:5000/v3/", link.get("href").asText());
		JsonNode medias = version.get("media-types");
		assertEquals(2, medias.size());
		for (JsonNode n : medias) {
			assertNotNull(n.get("base"));
			assertNotNull(n.get("type"));
		}
	}
}
