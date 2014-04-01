package com.infinities.keystone4j.intergrated.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.KeystoneApplication;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.DomainWrapper;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class DomainIntegratedTest extends AbstractIntegratedTest {

	private Domain domain, domain2;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2, role3;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		domain = new Domain();
		// domain.setId("domain1");
		domain.setDescription("desc of Domain");
		domain.setName("my domain");

		domain2 = new Domain();
		domain2.setId("domain2");
		domain2.setDescription("desc of Domain2");
		domain2.setName("my domain2");

		project = new Project();
		project.setId("project");
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);

		group = new Group();
		group.setId("newgroup");
		group.setDescription("my group");
		group.setName("example group");
		group.setDomain(domain);

		role1 = new Role();
		role1.setId("role1");
		role1.setDescription("my role1");
		role1.setName("example role1");

		role2 = new Role();
		role2.setId("role2");
		role2.setDescription("my role2");
		role2.setName("example role2");

		role3 = new Role();
		role3.setId("role1");
		role3.setDescription("my role3");
		role3.setName("example role3");

		return new KeystoneApplication();
	}

	@Test
	public void testCreateDomain() throws JsonProcessingException, IOException {

		DomainWrapper wrapper = new DomainWrapper(domain);
		String json = JsonUtils.toJson(wrapper);
		JsonNode node = JsonUtils.convertToJsonNode(json);
		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());

		Response response = target("/v3/domains").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());

		node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		domainJ = node.get("domain");
		assertNotNull(domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testListDomain() throws JsonProcessingException, IOException {

		Response response = target("/v3/domains").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode domainsJ = node.get("domains");
		assertEquals(2, domainsJ.size());
	}

	@Test
	public void testGetDomain() throws JsonProcessingException, IOException {
		domain.setId("default");

		Response response = target("/v3").path("domains").path(domain.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		JsonNode domainJ = node.get("domain");
		assertEquals("default", domainJ.get("id").asText());
		assertEquals("newdomain", domainJ.get("name").asText());
		assertEquals("my domain", domainJ.get("description").asText());
	}

	@Test
	public void testUpdateDomain() throws ClientProtocolException, IOException {
		domain.setId("69ea2c65-4679-441f-a596-8aec16752a0f");

		DomainWrapper wrapper = new DomainWrapper(domain);
		PatchClient client = new PatchClient("http://localhost:9998/v3/domains/" + domain.getId());
		JsonNode node = client.connect(wrapper);

		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testDeleteDomain() {
		domain.setId("69ea2c65-4679-441f-a596-8aec16752a0f");

		Response response = target("/v3").path("domains").path(domain.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreateGrantByUser() {
		domain.setId("default");
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreateGrantByGroup() {
		domain.setId("default");
		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());

	}

	@Test
	public void testCheckGrantByUser() {
		domain.setId("default");
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByGroup() {
		domain.setId("default");
		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByUser() throws JsonProcessingException, IOException {
		domain.setId("default");
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		role1.setName("admin");
		role1.setDescription("admin role");

		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		assertEquals(1, node.size());
		JsonNode roleJ = node.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	// @Test
	public void testListGrantByGroup() throws JsonProcessingException, IOException {
		domain.setId("default");
		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		role1.setName("admin");
		role1.setDescription("admin role");

		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		JsonNode node = JsonUtils.convertToJsonNode(response.readEntity(String.class));
		assertEquals(1, node.size());
		JsonNode roleJ = node.get(0);
		assertEquals(role1.getId(), roleJ.get("id").asText());
		assertEquals(role1.getName(), roleJ.get("name").asText());
		assertEquals(role1.getDescription(), roleJ.get("description").asText());
	}

	@Test
	public void testRevokeGrantByUser() {
		domain.setId("default");
		user.setId("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");

		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByGroup() {
		domain.setId("default");
		group.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
		role1.setId("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
