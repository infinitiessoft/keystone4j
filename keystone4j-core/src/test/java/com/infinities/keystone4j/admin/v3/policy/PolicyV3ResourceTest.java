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
package com.infinities.keystone4j.admin.v3.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.wrapper.PolicyWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class PolicyV3ResourceTest extends AbstractIntegratedTest {

	private Policy policy;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		policy = new Policy();
		policy.setId("e25be49f628f0a18a78db15d2b574d10fd9833f8d72f573b260b1e09b3bec637");
		policy.getBlob().put("default", false);
		policy.setType("ec2");
		policy.setProjectId("88e550a135bb4e6da68e79e5b7c4b2f2");
		policy.setUserId("e7912c2225e84ac5905d8cf0b5040a6d");

		return new PolicyResourceTestApplication();

	}

	@Test
	public void testCreatePolicy() throws JsonGenerationException, JsonMappingException, IOException {
		Policy policy = new Policy();
		policy.getBlob().put("default", false);
		policy.setType("ec2");
		policy.setProjectId("88e550a135bb4e6da68e79e5b7c4b2f2");
		policy.setUserId("e7912c2225e84ac5905d8cf0b5040a6d");

		PolicyWrapper wrapper = new PolicyWrapper(policy);

		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		System.err.println(json);

		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode policyJ = node.get("policy");
		System.err.println("blob: " + policyJ.get("blob").toString());

		assertTrue(policyJ.get("blob").hasNonNull("default"));
		assertEquals(policy.getBlob().get("default"), policyJ.get("blob").get("default").asBoolean());
		assertEquals(policy.getType(), policyJ.get("type").asText());
		assertEquals(policy.getUserId(), policyJ.get("user_id").asText());
		assertEquals(policy.getProjectId(), policyJ.get("project_id").asText());

		Response response = target("/v3/policies").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node);
		policyJ = node.get("policy");
		assertNotNull(policyJ.get("id").asText());
		assertTrue(policyJ.get("blob").hasNonNull("default"));
		assertEquals(policy.getBlob().get("default"), policyJ.get("blob").get("default").asBoolean());
		assertEquals(policy.getType(), policyJ.get("type").asText());
		assertEquals(policy.getProjectId(), policyJ.get("project_id").asText());
		assertEquals(policy.getUserId(), policyJ.get("user_id").asText());
	}

	@Test
	public void testListPolicies() throws JsonProcessingException, IOException {
		Response response = target("/v3/policies").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		JsonNode policiesJ = node.get("policies");
		assertEquals(1, policiesJ.size());
		JsonNode policyJ = policiesJ.get(0);
		assertNotNull(policyJ.get("id").asText());
		assertNotNull(policyJ.get("blob").asText());
		assertNotNull(policyJ.get("type").asText());
		assertNotNull(policyJ.get("user_id").asText());
		assertNotNull(policyJ.get("project_id").asText());
		assertNotNull(policyJ.get("links"));
		assertNotNull(policyJ.get("links").get("self").asText());
	}

	@Test
	public void testGetPolicy() throws JsonGenerationException, JsonMappingException, IOException {
		Response response = target("/v3/policies").path(policy.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());

		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		System.err.println(node.toString());
		JsonNode policyJ = node.get("policy");
		assertEquals(policy.getId(), policyJ.get("id").asText());
		assertEquals(policy.getType(), policyJ.get("type").asText());
		assertEquals(policy.getUserId(), policyJ.get("user_id").asText());
		assertEquals(policy.getProjectId(), policyJ.get("project_id").asText());
		assertTrue(policyJ.get("blob").hasNonNull("default"));
		assertEquals(policy.getBlob().get("default"), policyJ.get("blob").get("default").asBoolean());
		assertNotNull(policyJ.get("links"));
		assertNotNull(policyJ.get("links").get("self").asText());
	}

	@Test
	public void testUpdatePolicy() throws ClientProtocolException, IOException {
		String policyid = this.policy.getId();
		Policy policyUpdated = new Policy();
		policyUpdated.setUserId("e7912c2225e84ac5905d8cf0b5040a6f");
		policyUpdated.setType("newType");
		policyUpdated.getBlob().put("default", true);
		PolicyWrapper wrapper = new PolicyWrapper(policyUpdated);
		String json = JsonUtils.toJson(wrapper, Views.Advance.class);
		System.err.println(json);

		PatchClient client = new PatchClient("http://localhost:9998/v3/policies/" + policyid);
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode policyJ = node.get("policy");
		assertEquals(policyid, policyJ.get("id").asText());
		assertEquals(policyUpdated.getType(), policyJ.get("type").asText());
		assertEquals(policyUpdated.getUserId(), policyJ.get("user_id").asText());
		assertEquals(policy.getProjectId(), policyJ.get("project_id").asText());
		assertTrue(policyJ.get("blob").hasNonNull("default"));
		assertEquals(policyUpdated.getBlob().get("default"), policyJ.get("blob").get("default").asBoolean());
		assertNotNull(policyJ.get("links"));
		assertNotNull(policyJ.get("links").get("self").asText());
	}

	@Test
	public void testDeletePolicy() {
		Response response = target("/v3/policies").path(policy.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}
}
