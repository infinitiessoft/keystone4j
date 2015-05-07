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
//package com.infinities.keystone4j.driver;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.ws.rs.WebApplicationException;
//
//import org.jmock.Mockery;
//import org.jmock.integration.junit4.JUnit4Mockery;
//import org.jmock.lib.concurrent.Synchroniser;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.infinities.keystone4j.assignment.AssignmentDriver;
//import com.infinities.keystone4j.assignment.driver.AssignmentJpaDriver;
//import com.infinities.keystone4j.common.Config;
//import com.infinities.keystone4j.jpa.impl.GroupDomainGrantDao;
//import com.infinities.keystone4j.jpa.impl.GroupProjectGrantDao;
//import com.infinities.keystone4j.jpa.impl.UserDomainGrantDao;
//import com.infinities.keystone4j.jpa.impl.UserProjectGrantDao;
//import com.infinities.keystone4j.model.assignment.Assignment;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.GroupDomainGrant;
//import com.infinities.keystone4j.model.assignment.GroupProjectGrant;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.assignment.UserDomainGrant;
//import com.infinities.keystone4j.model.assignment.UserProjectGrant;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.token.Token;
//
//public class AssignmentJpaDriverTest extends AbstractDbUnitJpaTest {
//
//	private Mockery context;
//	private AssignmentDriver driver;
//	private Domain domain;
//	private Project project;
//	private User user;
//	private Token token;
//	private Group group;
//	private Role role;
//
//
//	@Before
//	public void setUp() throws Exception {
//		driver = new AssignmentJpaDriver();
//
//		context = new JUnit4Mockery() {
//
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//
//		domain = new Domain();
//		domain.setDescription("desc of Domain");
//		domain.setName("my domain");
//
//		project = new Project();
//		project.setDescription("desc of Project");
//		project.setDomain(domain);
//		project.setName("my project");
//
//		user = new User();
//		user.setId("newuser");
//		user.setDescription("my user");
//		user.setName("example user");
//		user.setDefault_project(project);
//		user.setDomain(domain);
//
//		token = new Token();
//		token.setProject(project);
//		token.setExpires(new Date());
//		token.setId("newtoken");
//		token.setIssueAt(new Date());
//		token.setUser(user);
//		user.getTokens().add(token);
//
//		group = new Group();
//		group.setDescription("my group");
//		group.setDomain(domain);
//		group.setName("newgroup");
//
//		role = new Role();
//		role.setDescription("my role1");
//		role.setName("example role1");
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testGetProject() {
//		Project ret = driver.getProject("79ea2c65-4679-441f-a596-8aec16752a2f");
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getId());
//		assertEquals("admin", ret.getName());
//		assertEquals("admin project", ret.getDescription());
//		assertNotNull(ret.getDomain());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test
//	public void testGetProjectByName() {
//		Project ret = driver.getProjectByName("admin", Config.Instance.getOpt(Config.Type.identity, "default_domain_id")
//				.asText());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getId());
//		assertEquals("admin", ret.getName());
//		assertEquals("admin project", ret.getDescription());
//		assertNotNull(ret.getDomain());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test
//	public void testListUsersForProject() {
//		List<User> rets = driver.listUsersForProject("79ea2c65-4679-441f-a596-8aec16752a2f");
//		assertEquals(1, rets.size());
//		User ret = rets.get(0);
//		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", ret.getId());
//		assertEquals("admin", ret.getName());
//		assertEquals("admin user", ret.getDescription());
//		assertEquals("admin@keystone4j.com", ret.getEmail());
//		assertEquals(
//				"c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec",
//				ret.getPassword());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getDefaultProjectId());
//		assertEquals("default", ret.getDomainid());
//	}
//
//	@Test
//	public void testListProjects() {
//		List<Project> rets = driver.listProjects(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		assertEquals(2, rets.size());
//		// Project ret = rets.get(0);
//		// assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getId());
//		// assertEquals("admin", ret.getName());
//		// assertEquals("admin project", ret.getDescription());
//		// assertNotNull(ret.getDomain());
//		// assertEquals(Config.Instance.getOpt(Config.Type.identity,
//		// "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test
//	public void testListProjectsForUser() {
//		List<Project> rets = driver.listProjectsForUser("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", new ArrayList<String>());
//		assertEquals(1, rets.size());
//		Project ret = rets.get(0);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getId());
//		assertEquals("admin", ret.getName());
//		assertEquals("admin project", ret.getDescription());
//		assertNotNull(ret.getDomain());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test(expected = WebApplicationException.class)
//	public void testAddRoleToUserAndProjectWithDuplicateRole() {
//		driver.addRoleToUserAndProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "79ea2c65-4679-441f-a596-8aec16752a2f",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//
//	}
//
//	@Test
//	public void testAddRoleToUserAndProject() {
//		assertEquals(
//				1,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		driver.addRoleToUserAndProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "79ea2c65-4679-441f-a596-8aec16752a2f",
//				"708bb4f9-9d3c-46af-b18c-7033dc022fff");
//		assertEquals(
//				2,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//	}
//
//	@Test
//	public void testRemoveRoleFromUserAndProject() {
//		assertEquals(
//				1,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		driver.removeRoleFromUserAndProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "79ea2c65-4679-441f-a596-8aec16752a2f",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		assertEquals(
//				0,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//	}
//
//	@Test
//	public void testListRoleAssignments() {
//		List<Assignment> rets = driver.listRoleAssignments();
//		assertEquals(4, rets.size());
//	}
//
//	@Test
//	public void testCreateProject() {
//		Project input = new Project();
//		input.setDescription("new desc");
//		input.setEnabled(false);
//		input.setName("new name");
//		Domain domain = new Domain();
//		domain.setId(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		input.setDomain(domain);
//		Project ret = driver.createProject(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEnabled(), ret.getEnabled());
//		assertNotNull(ret.getDomain());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test
//	public void testUpdateProject() {
//		Project input = new Project();
//		input.setDescription("update desc");
//		input.setEnabled(false);
//		input.setName("update name");
//		Project ret = driver.updateProject("79ea2c65-4679-441f-a596-8aec16752a2f", input);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a2f", ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEnabled(), ret.getEnabled());
//		assertNotNull(ret.getDomain());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getDomain().getId());
//	}
//
//	@Test
//	public void testDeleteProject() {
//		List<Project> ret = driver.listProjects(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		assertEquals(2, ret.size());
//		driver.deleteProject("79ea2c65-4679-441f-a596-8aec16752a2f");
//		ret = driver.listProjects(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		assertEquals(1, ret.size());
//	}
//
//	@Test
//	public void testCreateDomain() {
//		Domain input = new Domain();
//		input.setDescription("update desc");
//		input.setEnabled(false);
//		input.setName("update name");
//		Domain ret = driver.createDomain(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEnabled(), ret.getEnabled());
//	}
//
//	@Test
//	public void testListDomains() {
//		List<Domain> ret = driver.listDomains();
//		assertEquals(2, ret.size());
//		// Domain retDomain = ret.get(0);
//		// assertEquals(Config.Instance.getOpt(Config.Type.identity,
//		// "default_domain_id").asText(), retDomain.getId());
//		// assertEquals("newdomain", retDomain.getName());
//		// assertEquals("my domain", retDomain.getDescription());
//	}
//
//	@Test
//	public void testGetDomain() {
//		Domain ret = driver.getDomain(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getId());
//		assertEquals("newdomain", ret.getName());
//		assertEquals("my domain", ret.getDescription());
//	}
//
//	@Test
//	public void testGetDomainByName() {
//		Domain ret = driver.getDomainByName("newdomain");
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getId());
//		assertEquals("newdomain", ret.getName());
//		assertEquals("my domain", ret.getDescription());
//	}
//
//	@Test
//	public void testUpdateDomain() {
//		Domain input = new Domain();
//		input.setDescription("update desc");
//		input.setEnabled(false);
//		input.setName("update name");
//		Domain ret = driver.updateDomain(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), input);
//		assertEquals(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText(), ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEnabled(), ret.getEnabled());
//	}
//
//	@Test
//	public void testDeleteDomain() {
//		List<Domain> ret = driver.listDomains();
//		assertEquals(2, ret.size());
//		driver.deleteDomain(Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText());
//		ret = driver.listDomains();
//		assertEquals(1, ret.size());
//	}
//
//	@Test
//	public void testCreateRole() {
//		Role input = new Role();
//		input.setDescription("new role desc");
//		input.setName("new role");
//		Role ret = driver.createRole(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//	}
//
//	@Test
//	public void testListRoles() {
//		List<Role> rets = driver.listRoles();
//		assertEquals(2, rets.size());
//		// Role ret = rets.get(0);
//		// assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		// assertEquals("admin", ret.getName());
//		// assertEquals("admin role", ret.getDescription());
//	}
//
//	@Test
//	public void testGetRole() {
//		Role ret = driver.getRole("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		assertEquals("admin", ret.getName());
//		assertEquals("admin role", ret.getDescription());
//	}
//
//	@Test
//	public void testUpdateRole() {
//		Role input = new Role();
//		input.setName("update name");
//		input.setDescription("update description");
//		Role ret = driver.updateRole("708bb4f9-9d3c-46af-b18c-7033dc022ffb", input);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//	}
//
//	@Test
//	public void testDeleteRole() {
//		List<Role> ret = driver.listRoles();
//		assertEquals(2, ret.size());
//		driver.deleteRole("708bb4f9-9d3c-46af-b18c-7033dc022ffb");
//		ret = driver.listRoles();
//		assertEquals(1, ret.size());
//	}
//
//	@Test
//	public void testGetGroupProjectGrant() {
//		List<GroupProjectGrant> rets = driver.getGroupProjectGrants("708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//				"79ea2c65-4679-441f-a596-8aec16752a2f");
//		assertEquals(1, rets.size());
//		assertEquals("b5a71165-3e27-4085-b7ed-143630e58896", rets.get(0).getId());
//	}
//
//	@Test
//	public void testGetGroupDomainGrant() {
//		List<GroupDomainGrant> rets = driver.getGroupDomainGrants("708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default");
//		assertEquals(1, rets.size());
//		assertEquals("b5a71165-3e27-4085-b7ed-143630e58897", rets.get(0).getId());
//	}
//
//	@Test
//	public void testGetUserProjectGrant() {
//		List<UserProjectGrant> rets = driver.getUserProjectGrants("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//				"79ea2c65-4679-441f-a596-8aec16752a2f");
//		assertEquals(1, rets.size());
//		assertEquals("b5a71165-3e27-4085-b7ed-143630e58895", rets.get(0).getId());
//	}
//
//	@Test
//	public void testGetUserDomainGrant() {
//		List<UserDomainGrant> rets = driver.getUserDomainGrants("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default");
//		assertEquals(1, rets.size());
//		assertEquals("b5a71165-3e27-4085-b7ed-143630e58898", rets.get(0).getId());
//	}
//
//	@Test
//	public void testDeleteGrantByGroupDomain() {
//		assertEquals(1, driver.listGrantsByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default", false).size());
//		Role ret = driver.getGrantByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default", false);
//		assertNotNull(ret);
//		driver.deleteGrantByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb", "708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//				"default", false);
//		assertEquals(0, driver.listGrantsByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default", false).size());
//		// ret =
//		// driver.getGrantByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//		// "708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//		// "default", false);
//		// assertNull(ret);
//	}
//
//	@Test
//	public void testDeleteGrantByGroupProject() {
//		assertEquals(
//				1,
//				driver.listGrantsByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		Role ret = driver.getGrantByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffd", "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		driver.deleteGrantByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb", "708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//				"79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertEquals(
//				0,
//				driver.listGrantsByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		// ret =
//		// driver.getGrantByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//		// "708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//		// "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		// assertNull(ret);
//
//	}
//
//	@Test
//	public void testDeleteGrantByUserDomain() {
//		assertEquals(1, driver.listGrantsByUserDomain("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default", false).size());
//		Role ret = driver.getGrantByUserDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		driver.deleteGrantByUserDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb", "0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//				"default", false);
//		assertEquals(0, driver.listGrantsByUserDomain("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default", false).size());
//		// ret =
//		// driver.getGrantByUserDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//		// "0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//		// "default", false);
//		// assertNull(ret);
//	}
//
//	@Test
//	public void testDeleteGrantByUserProject() {
//		assertEquals(
//				1,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		Role ret = driver.getGrantByUserProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//		driver.deleteGrantByUserProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb", "0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//				"79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertEquals(
//				0,
//				driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//						"79ea2c65-4679-441f-a596-8aec16752a2f", false).size());
//		// ret =
//		// driver.getGrantByUserProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//		// "0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//		// "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		// assertNull(ret);
//	}
//
//	@Test
//	public void testGetGrantByGroupDomain() {
//		Role ret = driver.getGrantByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testGetGrantByGroupProject() {
//		Role ret = driver.getGrantByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"708bb4f9-9d3c-46af-b18c-7033dc022ffd", "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testGetGrantByUserDomain() {
//		Role ret = driver.getGrantByUserDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testGetGrantByUserProject() {
//		Role ret = driver.getGrantByUserProject("708bb4f9-9d3c-46af-b18c-7033dc022ffb",
//				"0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertNotNull(ret);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testCreateGrantByGroupDomain() {
//		assertEquals(1, new GroupDomainGrantDao().findAll().size());
//		driver.createGrantByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022fff", "708bb4f9-9d3c-46af-b18c-7033dc022ffc",
//				"default", false);
//		assertEquals(2, new GroupDomainGrantDao().findAll().size());
//	}
//
//	@Test
//	public void testCreateGrantByGroupProject() {
//		assertEquals(1, new GroupProjectGrantDao().findAll().size());
//		driver.createGrantByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022fff", "708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//				"79ea2c65-4679-441f-a596-8aec16752a20");
//		assertEquals(2, new GroupProjectGrantDao().findAll().size());
//	}
//
//	@Test
//	public void testCreateGrantByUserDomain() {
//		assertEquals(1, new UserDomainGrantDao().findAll().size());
//		driver.createGrantByUserDomain("708bb4f9-9d3c-46af-b18c-7033dc022fff", "0f3328f8-a7e7-41b4-830d-be8fdd5186c8",
//				"default", false);
//		assertEquals(2, new UserDomainGrantDao().findAll().size());
//	}
//
//	@Test
//	public void testCreateGrantByUserProject() {
//		assertEquals(1, new UserProjectGrantDao().findAll().size());
//		driver.createGrantByUserProject("708bb4f9-9d3c-46af-b18c-7033dc022fff", "0f3328f8-a7e7-41b4-830d-be8fdd5186c8",
//				"79ea2c65-4679-441f-a596-8aec16752a20");
//		assertEquals(2, new UserProjectGrantDao().findAll().size());
//	}
//
//	@Test
//	public void testListGrantsByGroupDomain() {
//		List<Role> rets = driver.listGrantsByGroupDomain("708bb4f9-9d3c-46af-b18c-7033dc022ffd", "default", false);
//		assertEquals(1, rets.size());
//		Role ret = rets.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testListGrantsByGroupProject() {
//		List<Role> rets = driver.listGrantsByGroupProject("708bb4f9-9d3c-46af-b18c-7033dc022ffd",
//				"79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertEquals(1, rets.size());
//		Role ret = rets.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testListGrantsByUserProject() {
//		List<Role> rets = driver.listGrantsByUserProject("0f3328f8-a7e7-41b4-830d-be8fdd5186c7",
//				"79ea2c65-4679-441f-a596-8aec16752a2f", false);
//		assertEquals(1, rets.size());
//		Role ret = rets.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
//	@Test
//	public void testListGrantsByUserDomain() {
//		List<Role> rets = driver.listGrantsByUserDomain("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "default", false);
//		assertEquals(1, rets.size());
//		Role ret = rets.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffb", ret.getId());
//	}
//
// }
