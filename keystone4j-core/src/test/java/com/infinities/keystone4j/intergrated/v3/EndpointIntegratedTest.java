package com.infinities.keystone4j.intergrated.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.JacksonFeature;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.ObjectMapperResolver;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.common.Config;

public class EndpointIntegratedTest extends AbstractIntegratedTest {

	private Endpoint endpoint;
	private Service service;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		service = new Service();
		service.setId("79ea2c65-4679-441f-a596-8aec16752a0f");

		endpoint = new Endpoint();
		endpoint.setInterfaceType("internal");
		endpoint.setName("the internal volume endpoint");
		endpoint.setUrl("http://identity:35357/v3/endpoints/");
		endpoint.setService(service);

		return new KeystoneApplication();
	}

	@Test
	public void testCreateEndpoint() throws JsonProcessingException, IOException {
		EndpointWrapper wrapper = new EndpointWrapper(endpoint);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());

		Response response = target("/v3/endpoints").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		endpointJ = node.get("endpoint");
		assertNotNull(endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());

	}

	@Test
	public void testListEndpoints() throws JsonProcessingException, IOException {
		final List<Endpoint> endpoints = new ArrayList<Endpoint>();
		endpoint.setId("endpoint1");
		endpoints.add(endpoint);
		Response response = target("/v3/endpoints").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode endpointsJ = node.get("endpoints");
		assertEquals(1, endpointsJ.size());
		JsonNode endpointJ = endpointsJ.get(0);
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a1f", endpointJ.get("id").asText());
		assertEquals("keystone_endpoint", endpointJ.get("name").asText());
		assertEquals("public", endpointJ.get("interface").asText());
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", endpointJ.get("service_id").asText());
		assertEquals("http://localhost:%(public_port)/", endpointJ.get("url").asText());
	}

	@Test
	public void testGetEndpoint() throws JsonProcessingException, IOException {
		endpoint.setId("79ea2c65-4679-441f-a596-8aec16752a1f");

		Response response = target("/v3/endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode endpointJ = node.get("endpoint");
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a1f", endpointJ.get("id").asText());
		assertEquals("keystone_endpoint", endpointJ.get("name").asText());
		assertEquals("public", endpointJ.get("interface").asText());
		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", endpointJ.get("service_id").asText());
		assertEquals("http://localhost:%(public_port)/", endpointJ.get("url").asText());
	}

	@Test
	public void testUpdateEndpoint() throws ClientProtocolException, IOException {
		endpoint.setId("79ea2c65-4679-441f-a596-8aec16752a1f");
		EndpointWrapper wrapper = new EndpointWrapper(endpoint);
		PatchClient client = new PatchClient("http://localhost:9998/v3/endpoints/" + endpoint.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getId(), endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());

	}

	@Test
	public void testDeleteEndpoint() {
		endpoint.setId("79ea2c65-4679-441f-a596-8aec16752a1f");
		Response response = target("/v3").path("endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
