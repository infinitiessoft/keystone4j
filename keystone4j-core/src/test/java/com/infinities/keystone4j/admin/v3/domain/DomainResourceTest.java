package com.infinities.keystone4j.admin.v3.domain;

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
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.DomainWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class DomainResourceTest extends JerseyTest {

	private Domain domain;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2;


	// private GroupProjectGrantMetadata groupProjectGrantMetadata;

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		domain = new Domain();
		domain.setId("default");
		domain.setDescription("Owns users and tenants (i.e. projects) available on Identity API v2.");
		domain.setName("Default");

		project = new Project();
		project.setId("project");
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefaultProject(project);
		user.setDomain(domain);

		group = new Group();
		group.setId("88e550a135bb4e6da68e79e5b7c4b2f3");
		group.setName("demo");
		group.setDomain(domain);

		role1 = new Role();
		role1.setId("9fe2ff9ee4384b1894a90878d3e92bab");

		role2 = new Role();
		role2.setId("d903936e7bbd4183b8cd35816d2cf88b");

		return new DomainResourceTestApplication();

	}

	@Test
	public void testCreateDomain() throws JsonProcessingException, IOException {
		Domain domain = new Domain();
		domain.setDescription("domain desc");
		domain.setName("demo domain");

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
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		node = JsonUtils.convertToJsonNode(ret);
		domainJ = node.get("domain");
		assertNotNull(domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testListDomain() throws JsonProcessingException, IOException {
		final List<Domain> domains = new ArrayList<Domain>();
		domains.add(domain);

		Response response = target("/v3/domains").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode domainsJ = node.get("domains");
		assertEquals(1, domainsJ.size());
		JsonNode domainJ = domainsJ.get(0);
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testGetDomain() throws JsonProcessingException, IOException {
		Response response = target("/v3").path("domains").path(domain.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testUpdateDomain() throws ClientProtocolException, IOException {
		domain.setName("domain1");

		DomainWrapper wrapper = new DomainWrapper(domain);
		PatchClient client = new PatchClient("http://localhost:9998/v3/domains/" + domain.getId());
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());

		JsonNode domainJ = node.get("domain");
		assertEquals(domain.getId(), domainJ.get("id").asText());
		assertEquals(domain.getName(), domainJ.get("name").asText());
		assertEquals(domain.getDescription(), domainJ.get("description").asText());
	}

	@Test
	public void testDeleteDomain() throws JsonProcessingException, IOException {
		Domain domain = new Domain();
		domain.setDescription("domain desc");
		domain.setName("demo domain");
		domain.setEnabled(false);

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
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		node = JsonUtils.convertToJsonNode(ret);
		domainJ = node.get("domain");
		String domainid = domainJ.get("id").asText();
		response = target("/v3").path("domains").path(domainid).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testListGrantByUser() throws JsonProcessingException, IOException {
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleJ = node.get("roles");
		assertEquals(1, roleJ.size());
	}

	@Test
	public void testListGrantByGroup() throws JsonProcessingException, IOException {
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role1);

		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode roleJ = node.get("roles");
		assertEquals(1, roleJ.size());
	}

	@Test
	public void testCreateGrantByUser() {
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role2.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCreateGrantByGroup() {
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role2.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.put(Entity.json(""));
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByUser() {
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testCheckGrantByGroup() {
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).head();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByUser() {
		Response response = target("/v3/domains").path(domain.getId()).path("users").path(user.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

	@Test
	public void testRevokeGrantByGroup() {
		Response response = target("/v3/domains").path(domain.getId()).path("groups").path(group.getId()).path("roles")
				.path(role1.getId()).register(JacksonFeature.class).register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}

}
