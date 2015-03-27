package com.infinities.keystone4j.admin.v3.credential;

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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.PatchClient;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.credential.Blob;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.credential.wrapper.CredentialWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class CredentialResourceTest extends AbstractIntegratedTest {

	private Domain defaultDomain;
	private User user;
	private Group group;
	private Project project;
	private Role role1, role2;
	private Credential credential;
	private Blob blob;


	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		defaultDomain = new Domain();
		defaultDomain.setId("default");
		defaultDomain.setDescription("desc of default Domain");
		defaultDomain.setName("my default domain");

		defaultDomain = new Domain();
		defaultDomain.setId("default");
		defaultDomain.setDescription("desc of default Domain");
		defaultDomain.setName("my default domain");

		project = new Project();
		project.setId("88e550a135bb4e6da68e79e5b7c4b2f2");
		project.setDomain(defaultDomain);
		project.setName("admin");

		user = new User();
		user.setId("e7912c2225e84ac5905d8cf0b5040a6d");
		user.setName("admin");
		user.setDescription("admin user");
		user.setDomain(defaultDomain);
		user.setDefaultProject(project);

		group = new Group();
		group.setId("88e550a135bb4e6da68e79e5b7c4b2f3");
		group.setName("demo");
		group.setDomain(defaultDomain);

		role1 = new Role();
		role1.setId("9fe2ff9ee4384b1894a90878d3e92bab");
		role1.setName("_member_");
		role1.setDescription("Default role for project membership");

		role2 = new Role();
		role2.setId("d903936e7bbd4183b8cd35816d2cf88b");

		blob = new Blob();
		blob.setAccess("access");
		blob.setSecret("secret");
		credential = new Credential();
		String blobStr = null;
		try {
			blobStr = JsonUtils.toJsonWithoutPrettyPrint(blob);
		} catch (Exception e) {
			new RuntimeException(e);
		}
		credential.setBlob(blobStr);
		credential.setType("ec2");
		credential.setUserId("e7912c2225e84ac5905d8cf0b5040a6d");
		credential.setProjectId("88e550a135bb4e6da68e79e5b7c4b2f2");
		credential.setId("e25be49f628f0a18a78db15d2b573d10fd9833f8d72f573b260b1e09b3bec637");

		return new CredentialResourceTestApplication();

	}

	@Test
	public void testCreateCredential() throws JsonGenerationException, JsonMappingException, IOException {
		Credential credential = new Credential();
		Blob blob = new Blob();
		blob.setAccess("access");
		blob.setSecret("secret");
		String blobStr = JsonUtils.toJsonWithoutPrettyPrint(blob);
		System.err.println(blobStr);
		credential.setBlob(blobStr);
		credential.setProjectId(project.getId());
		credential.setType("ec2");
		credential.setUserId(user.getId());
		CredentialWrapper wrapper = new CredentialWrapper(credential);
		System.err.println(JsonUtils.toJsonWithoutPrettyPrint(wrapper));

		Response response = target("/v3/credentials").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText())
				.post(Entity.entity(wrapper, MediaType.APPLICATION_JSON_TYPE));
		assertEquals(201, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		JsonNode credentialJ = node.get("credential");
		assertNotNull(credentialJ.get("id").asText());
		assertEquals(blobStr, credentialJ.get("blob").asText());
		assertEquals(credential.getUserId(), credentialJ.get("user_id").asText());
		assertEquals(credential.getProjectId(), credentialJ.get("project_id").asText());
		assertNotNull(credentialJ.get("links"));
		assertNotNull(credentialJ.get("links").get("self").asText());
	}

	@Test
	public void testListCredentials() throws JsonProcessingException, IOException {
		String blobStr = JsonUtils.toJsonWithoutPrettyPrint(blob);
		System.err.println("blob: " + blobStr);
		Response response = target("/v3/credentials").register(JacksonFeature.class).register(ObjectMapperResolver.class)
				.request().header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		JsonNode credentialsJ = node.get("credentials");
		assertEquals(1, credentialsJ.size());
		JsonNode credentialJ = credentialsJ.get(0);
		assertNotNull(credentialJ.get("id").asText());
		assertEquals(credential.getType(), credentialJ.get("type").asText());
		assertEquals(credential.getProjectId(), credentialJ.get("project_id").asText());
		assertEquals(credential.getUserId(), credentialJ.get("user_id").asText());
		assertEquals(blobStr, credentialJ.get("blob").asText());
		assertNotNull(credentialJ.get("links"));
		assertNotNull(credentialJ.get("links").get("self").asText());
	}

	@Test
	public void testGetCredential() throws JsonGenerationException, JsonMappingException, IOException {
		String blobStr = JsonUtils.toJsonWithoutPrettyPrint(blob);
		Response response = target("/v3/credentials/" + credential.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		System.err.println(ret);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		JsonNode credentialJ = node.get("credential");
		assertEquals(credential.getId(), credentialJ.get("id").asText());
		assertEquals(credential.getType(), credentialJ.get("type").asText());
		assertEquals(credential.getProjectId(), credentialJ.get("project_id").asText());
		assertEquals(credential.getUserId(), credentialJ.get("user_id").asText());
		assertEquals(blobStr, credentialJ.get("blob").asText());
	}

	@Test
	public void testUpdateCredentail() throws ClientProtocolException, IOException {
		String blobStr = JsonUtils.toJsonWithoutPrettyPrint(blob);
		Credential c = new Credential();
		c.setType("s3");
		CredentialWrapper wrapper = new CredentialWrapper(c);
		System.err.println(JsonUtils.toJson(wrapper));
		PatchClient client = new PatchClient("http://localhost:9998/v3/credentials/" + credential.getId());
		JsonNode node = client.connect(wrapper);
		System.err.println(node.toString());
		JsonNode credentialJ = node.get("credential");
		assertEquals(credential.getId(), credentialJ.get("id").asText());
		assertEquals("s3", credentialJ.get("type").asText());
		assertEquals(credential.getProjectId(), credentialJ.get("project_id").asText());
		assertEquals(credential.getUserId(), credentialJ.get("user_id").asText());
		assertEquals(blobStr, credentialJ.get("blob").asText());
	}

	@Test
	public void testDeleteCredential() {
		Response response = target("/v3/credentials/" + credential.getId()).register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).delete();
		assertEquals(204, response.getStatus());
	}
}
