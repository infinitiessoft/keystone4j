package com.infinities.keystone4j.admin.v3.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.wrapper.ServiceWrapper;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class ServiceResourceTest extends AbstractIntegratedTest {

	private Service service;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		service = new Service();
		service.setDescription("Openstack Identity Service");
		service.setName("keystone");
		service.setType("identity");
		service.setId("3f813b99431e46c0af877393f6ad91d7");
		return new ServiceResourceTestApplication();

	}

	// +endpoints
	@Test
	public void testCreateService() throws JsonGenerationException, JsonMappingException, IOException {
		Service service = new Service();
		service.setDescription("demo service");
		service.setName("demo");
		service.setType("service");
		ServiceWrapper wrapper = new ServiceWrapper(service);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode serviceJ = node.get("service");
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		node = JsonUtils.convertToJsonNode(ret);
		serviceJ = node.get("service");
		// assertEquals(id, serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());
	}

	@Test
	public void testListServices() throws JsonProcessingException, IOException {
		final List<Service> services = new ArrayList<Service>();
		services.add(service);

		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String res = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(res);
		JsonNode servicesJ = node.get("services");
		System.err.println(res);
		assertEquals(1, servicesJ.size());
		JsonNode serviceJ = servicesJ.get(0);
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

	}

	@Test
	public void testGetService() throws JsonProcessingException, IOException {
		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode serviceJ = node.get("service");
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());
		System.err.println(ret);
	}

	@Test
	public void testUpdateService() throws JsonProcessingException, IOException {
		ServiceWrapper wrapper = new ServiceWrapper(service);
		service.setName("demo");
		PatchClient client = new PatchClient("http://localhost:9998/v3/services/" + service.getId());
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode serviceJ = node.get("service");
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

	}

	@Test
	public void testDeleteService() {
		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
