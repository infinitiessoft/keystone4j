package com.infinities.keystone4j.resource.v3.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.JacksonFeature;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.ObjectMapperResolver;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.catalog.model.ServiceWrapper;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ServiceResourceTest extends JerseyTest {

	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;
	private CatalogApi catalogApi;
	private Service service;


	@Override
	protected Application configure() {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		tokenApi = context.mock(TokenApi.class);
		tokenProviderApi = context.mock(TokenProviderApi.class);
		assignmentApi = context.mock(AssignmentApi.class);
		identityApi = context.mock(IdentityApi.class);
		policyApi = context.mock(PolicyApi.class);
		trustApi = context.mock(TrustApi.class);
		catalogApi = context.mock(CatalogApi.class);

		service = new Service();
		service.setDescription("Keystone Identity Service");
		service.setName("keystone");
		service.setType("identity");

		return new ServiceResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);

	}

	@Test
	public void testCreateService() throws JsonGenerationException, JsonMappingException, IOException {
		final String id = "newservice";
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).createService(service);
				will(new CustomAction("add id to service") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Service service = (Service) invocation.getParameter(0);
						service.setId(id);
						return service;
					}

				});
			}
		});

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

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		serviceJ = node.get("service");
		assertEquals(id, serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

	}

	@Test
	public void testListServices() throws JsonProcessingException, IOException {
		final List<Service> services = new ArrayList<Service>();
		service.setId("service1");
		services.add(service);

		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).listServices();
				will(returnValue(services));
			}
		});
		Response response = target("/v3/services").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode servicesJ = node.get("services");
		assertEquals(1, servicesJ.size());
		JsonNode serviceJ = servicesJ.get(0);
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

	}

	@Test
	public void testGetService() throws JsonProcessingException, IOException {
		service.setId("service1");
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).getService(service.getId());
				will(returnValue(service));
			}
		});
		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode serviceJ = node.get("service");
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());
	}

	@Test
	public void testUpdateService() throws JsonProcessingException, IOException {
		service.setId("service1");
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).updateService(service.getId(), service);
				will(returnValue(service));
			}
		});
		ServiceWrapper wrapper = new ServiceWrapper(service);
		PatchClient client = new PatchClient("http://localhost:9998/v3/services/" + service.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode serviceJ = node.get("service");
		assertEquals(service.getId(), serviceJ.get("id").asText());
		assertEquals(service.getName(), serviceJ.get("name").asText());
		assertEquals(service.getDescription(), serviceJ.get("description").asText());
		assertEquals(service.getType(), serviceJ.get("type").asText());

	}

	@Test
	public void testDeleteService() {
		service.setId("service1");
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).deleteService(service.getId());
				will(returnValue(service));
			}
		});
		Response response = target("/v3").path("services").path(service.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
