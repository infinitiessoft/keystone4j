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
package com.infinities.keystone4j.admin.v3.ednpoint;

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
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointWrapper;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class EndpointResourceTest extends AbstractIntegratedTest {

	private Endpoint endpoint;
	private Service service;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		service = new Service();
		service.setDescription("Keystone Identity Service");
		service.setName("keystone");
		service.setType("identity");
		service.setId("newserviceid");

		endpoint = new Endpoint();
		endpoint.setId("9775f17cbc814f8d8097a524680bb33c");
		endpoint.setInterfaceType("admin");
		endpoint.setName("keystone_endpoint");
		endpoint.setDescription("keystone endpoint");
		endpoint.setUrl("http://localhost:35357/v3.0/");
		endpoint.setServiceid("3f813b99431e46c0af877393f6ad91d7");
		endpoint.setRegionid("RegionOne");

		return new EndpointResourceTestApplication();

	}

	@Test
	public void testCreateEndpoint() throws JsonProcessingException, IOException {
		Endpoint endpoint = new Endpoint();
		endpoint.setInterfaceType("public");
		endpoint.setName("public_endpoint");
		endpoint.setDescription("public endpoint");
		endpoint.setUrl("http://localhost:35357/v3.0/");
		endpoint.setServiceid("3f813b99431e46c0af877393f6ad91d7");
		endpoint.setRegionid("RegionOne");
		EndpointWrapper wrapper = new EndpointWrapper(endpoint);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());

		Response response = target("/v3/endpoints").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		node = JsonUtils.convertToJsonNode(ret);
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
		endpoints.add(endpoint);
		Response response = target("/v3/endpoints").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
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
		Response response = target("/v3/endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getId(), endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());
	}

	@Test
	public void testUpdateEndpoint() throws ClientProtocolException, IOException {
		endpoint.setName("newName");
		EndpointWrapper wrapper = new EndpointWrapper(endpoint);
		PatchClient client = new PatchClient("http://localhost:9998/v3/endpoints/" + endpoint.getId());
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode endpointJ = node.get("endpoint");
		assertEquals(endpoint.getId(), endpointJ.get("id").asText());
		assertEquals(endpoint.getName(), endpointJ.get("name").asText());
		assertEquals(endpoint.getInterfaceType(), endpointJ.get("interface").asText());
		assertEquals(endpoint.getServiceid(), endpointJ.get("service_id").asText());
		assertEquals(endpoint.getUrl(), endpointJ.get("url").asText());

	}

	@Test
	public void testDeleteEndpoint() {
		Response response = target("/v3").path("endpoints").path(endpoint.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
