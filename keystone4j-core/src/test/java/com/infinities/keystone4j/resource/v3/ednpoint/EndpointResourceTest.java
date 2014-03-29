package com.infinities.keystone4j.resource.v3.ednpoint;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.JacksonFeature;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.ObjectMapperResolver;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class EndpointResourceTest extends JerseyTest {

	private Mockery context;
	private TokenApi tokenApi;
	private TokenProviderApi tokenProviderApi;
	private AssignmentApi assignmentApi;
	private IdentityApi identityApi;
	private PolicyApi policyApi;
	private TrustApi trustApi;
	private CatalogApi catalogApi;
	private Endpoint endpoint;
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
		service.setId("newserviceid");

		endpoint = new Endpoint();
		endpoint.setInterfaceType("internal");
		endpoint.setName("the internal volume endpoint");
		endpoint.setUrl("http://identity:35357/v3/endpoints/");
		endpoint.setService(service);

		return new EndpointResourceTestApplication(catalogApi, tokenApi, tokenProviderApi, assignmentApi, identityApi,
				policyApi, trustApi);

	}

	@Test
	public void testCreateEndpoint() throws JsonProcessingException, IOException {
		final String id = "newendpoint";
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).createEndpoint(endpoint);
				will(new CustomAction("add id to endpoint") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Endpoint endpoint = (Endpoint) invocation.getParameter(0);
						endpoint.setId(id);
						return endpoint;
					}

				});
			}
		});

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
		assertEquals(id, endpointJ.get("id").asText());
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

		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).listEndpoints();
				will(returnValue(endpoints));
			}
		});
		Response response = target("/v3/endpoints").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode endpointsJ = node.get("endpoints");
		assertEquals(1, endpointsJ.size());
		JsonNode endpointJ = endpointsJ.get(0);
		assertEquals(endpoint.getId(), endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());
	}

	@Test
	public void testGetEndpoint() throws JsonProcessingException, IOException {
		endpoint.setId("endpoint1");

		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).getEndpoint(endpoint.getId());
				will(returnValue(endpoint));
			}
		});
		Response response = target("/v3/endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getId(), endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());
	}

	@Test
	public void testUpdateEndpoint() throws ClientProtocolException, IOException {
		endpoint.setId("endpoint1");
		endpoint.setName("newName");
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).updateEndpoint(endpoint.getId(), endpoint);
				will(returnValue(endpoint));
			}
		});
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
		final Endpoint endpoint = new Endpoint();
		endpoint.setId("endpoint1");
		context.checking(new Expectations() {

			{
				exactly(1).of(catalogApi).deleteEndpoint(endpoint.getId());
				will(returnValue(endpoint));
			}
		});
		Response response = target("/v3").path("endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
