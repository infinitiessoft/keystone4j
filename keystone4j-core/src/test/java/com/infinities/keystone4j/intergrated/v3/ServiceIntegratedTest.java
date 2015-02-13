//package com.infinities.keystone4j.intergrated.v3;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.io.IOException;
//
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.Application;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.glassfish.jersey.test.TestProperties;
//import org.junit.Test;
//
//import com.fasterxml.jackson.core.JsonGenerationException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.infinities.keystone4j.KeystoneApplication;
//import com.infinities.keystone4j.PatchClient;
//import com.infinities.keystone4j.common.Config;
//import com.infinities.keystone4j.model.catalog.Service;
//import com.infinities.keystone4j.model.catalog.ServiceWrapper;
//import com.infinities.keystone4j.utils.jackson.JacksonFeature;
//import com.infinities.keystone4j.utils.jackson.JsonUtils;
//import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;
//
//public class ServiceIntegratedTest extends AbstractIntegratedTest {
//
//	private Service service;
//	private final String baseUrl = "http://localhost:8080/v3/services";
//
//
//	@Override
//	protected Application configure() {
//		enable(TestProperties.LOG_TRAFFIC);
//		enable(TestProperties.DUMP_ENTITY);
//
//		service = new Service();
//		service.setDescription("Nova compute Service");
//		service.setName("nova");
//		service.setType("compute");
//
//		return new KeystoneApplication();
//	}
//
//	@Test
//	public void testCreateService() throws JsonGenerationException, JsonMappingException, IOException {
//		// final String id = "newservice";
//
//		ServiceWrapper wrapper = new ServiceWrapper(service, baseUrl);
//		String json = JsonUtils.toJson(wrapper);
//		JsonNode node = JsonUtils.convertToJsonNode(json);
//		JsonNode serviceJ = node.get("service");
//		assertEquals(service.getName(), serviceJ.get("name").asText());
//		assertEquals(service.getDescription(), serviceJ.get("description").asText());
//		assertEquals(service.getType(), serviceJ.get("type").asText());
//
//		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
//				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
//		assertEquals(201, response.getStatus());
//
//		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		serviceJ = node.get("service");
//		assertNotNull(serviceJ.get("id").asText());
//		assertEquals("nova", serviceJ.get("name").asText());
//		assertEquals("Nova compute Service", serviceJ.get("description").asText());
//		assertEquals("compute", serviceJ.get("type").asText());
//
//	}
//
//	@Test
//	public void testListServices() throws JsonProcessingException, IOException {
//		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
//				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode servicesJ = node.get("services");
//		assertEquals(1, servicesJ.size());
//		JsonNode serviceJ = servicesJ.get(0);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", serviceJ.get("id").asText());
//		assertEquals("keystone", serviceJ.get("name").asText());
//		assertEquals("identity service", serviceJ.get("description").asText());
//		assertEquals("identity", serviceJ.get("type").asText());
//
//	}
//
//	@Test
//	public void testGetService() throws JsonProcessingException, IOException {
//		service.setId("79ea2c65-4679-441f-a596-8aec16752a0f");
//		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
//		assertEquals(200, response.getStatus());
//		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
//		JsonNode serviceJ = node.get("service");
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", serviceJ.get("id").asText());
//		assertEquals("keystone", serviceJ.get("name").asText());
//		assertEquals("identity service", serviceJ.get("description").asText());
//		assertEquals("identity", serviceJ.get("type").asText());
//	}
//
//	@Test
//	public void testUpdateService() throws JsonProcessingException, IOException {
//		service.setId("79ea2c65-4679-441f-a596-8aec16752a0f");
//		ServiceWrapper wrapper = new ServiceWrapper(service, baseUrl);
//		PatchClient client = new PatchClient("http://localhost:9998/v3/services/" + service.getId());
//		JsonNode node = client.connect(wrapper);
//
//		JsonNode serviceJ = node.get("service");
//		assertEquals(service.getId(), serviceJ.get("id").asText());
//		assertEquals(service.getName(), serviceJ.get("name").asText());
//		assertEquals(service.getDescription(), serviceJ.get("description").asText());
//		assertEquals(service.getType(), serviceJ.get("type").asText());
//
//	}
//
//	@Test
//	public void testDeleteService() {
//		service.setId("79ea2c65-4679-441f-a596-8aec16752a0f");
//		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
//				.register(ObjectMapperResolver.class).request()
//				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
//		assertEquals(204, response.getStatus());
//	}
//
// }
