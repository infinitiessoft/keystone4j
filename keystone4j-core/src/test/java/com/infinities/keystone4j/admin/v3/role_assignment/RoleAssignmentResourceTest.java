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
package com.infinities.keystone4j.admin.v3.role_assignment;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.intergrated.v3.AbstractIntegratedTest;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserGroupMembership;
import com.infinities.keystone4j.utils.JsonUtils;
import com.infinities.keystone4j.utils.jackson.JacksonFeature;
import com.infinities.keystone4j.utils.jackson.ObjectMapperResolver;

public class RoleAssignmentResourceTest extends AbstractIntegratedTest {

	private Domain defaultDomain;
	private User user, user2;
	private Group group;
	private Project project;
	private Role role1, role2;
	private UserGroupMembership userGroupMembership;


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

		user2 = new User();
		user2.setId("e7912c2225e84ac5905d8cf0b5040a6f");
		user2.setName("demo");
		user2.setDescription("demo user");
		user2.setDomain(defaultDomain);
		user2.setDefaultProject(project);

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

		userGroupMembership = new UserGroupMembership();
		userGroupMembership.setDescription("my usergroupmembership");
		userGroupMembership.setId("newgroupmembership");
		userGroupMembership.setUser(user);
		userGroupMembership.setGroup(group);

		group.getUserGroupMemberships().add(userGroupMembership);

		return new RoleAssignmentResourceTestApplication();
	}

	@Test
	public void testListRoleAssignment() throws JsonProcessingException, IOException {
		Response response = target("/v3/role_assignments").register(JacksonFeature.class)
				.register(ObjectMapperResolver.class).request()
				.header("X-Auth-Token", Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText()).get();
		assertEquals(200, response.getStatus());
		String ret = response.readEntity(String.class);
		JsonNode node = JsonUtils.convertToJsonNode(ret);
		System.err.println(ret);
		JsonNode rolesJ = node.get("role_assignments");
		assertEquals(4, rolesJ.size());
	}

}
