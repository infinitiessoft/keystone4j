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
//import com.infinities.keystone4j.identity.IdentityDriver;
//import com.infinities.keystone4j.identity.driver.IdentityJpaDriver;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//
//public class IdentityJpaDriverTest extends AbstractDbUnitJpaTest {
//
//	private Mockery context;
//	private IdentityDriver driver;
//
//
//	@Before
//	public void setUp() throws Exception {
//
//		driver = new IdentityJpaDriver();
//
//		context = new JUnit4Mockery() {
//
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testAuthenticate() {
//		User ret = driver.authenticate("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "admin");
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
//	public void testCreateUser() {
//		User input = new User();
//		input.setDescription("my user");
//		input.setName("example user");
//		input.setEmail("input@demo.com");
//		input.setPassword("password");
//		Project project = new Project();
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a20");
//		input.setDefault_project(project);
//		Domain domain = new Domain();
//		domain.setId("default");
//		input.setDomain(domain);
//
//		User ret = driver.createUser(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEmail(), ret.getEmail());
//		assertEquals(input.getPassword(), ret.getPassword());
//		assertEquals(input.getDefaultProjectId(), ret.getDefaultProjectId());
//		assertEquals(input.getDomainid(), ret.getDomainid());
//	}
//
//	@Test
//	public void testListUsers() {
//		List<User> rets = driver.listUsers();
//		assertEquals(2, rets.size());
//	}
//
//	@Test
//	public void testListUsersInGroup() {
//		List<User> rets = driver.listUsersInGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		assertEquals(1, rets.size());
//		User ret = rets.get(0);
//		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", ret.getId());
//		assertEquals("demo", ret.getName());
//		assertEquals("demo user", ret.getDescription());
//		assertEquals("demo@keystone4j.com", ret.getEmail());
//		assertEquals("demo", ret.getPassword());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a20", ret.getDefaultProjectId());
//		assertEquals("default", ret.getDomainid());
//	}
//
//	@Test
//	public void testGetUser() {
//		User ret = driver.getUser("0f3328f8-a7e7-41b4-830d-be8fdd5186c7");
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
//	public void testUpdateUser() {
//		User input = new User();
//		input.setDescription("my user");
//		input.setName("example user");
//		input.setEmail("input@demo.com");
//		input.setPassword("password");
//		Project project = new Project();
//		project.setId("79ea2c65-4679-441f-a596-8aec16752a20");
//		input.setDefault_project(project);
//		Domain domain = new Domain();
//		domain.setId("default");
//		input.setDomain(domain);
//
//		User ret = driver.updateUser("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", input);
//		assertEquals("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getEmail(), ret.getEmail());
//		assertEquals(input.getPassword(), ret.getPassword());
//		assertEquals(input.getDefaultProjectId(), ret.getDefaultProjectId());
//		assertEquals(input.getDomainid(), ret.getDomainid());
//	}
//
//	@Test
//	public void testAddUserToGroup() {
//		List<User> rets = driver.listUsersInGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		assertEquals(0, rets.size());
//		driver.addUserToGroup("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		rets = driver.listUsersInGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		assertEquals(1, rets.size());
//	}
//
//	@Test(expected = WebApplicationException.class)
//	public void testCheckUserInGroupFailed() {
//		driver.checkUserInGroup("0f3328f8-a7e7-41b4-830d-be8fdd5186c7", "708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//	}
//
//	@Test
//	public void testCheckUserInGroup() {
//		driver.checkUserInGroup("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", "708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//	}
//
//	@Test
//	public void testRemoveUserFromGroup() {
//		List<User> rets = driver.listUsersInGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		assertEquals(1, rets.size());
//		driver.removeUserFromGroup("0f3328f8-a7e7-41b4-830d-be8fdd5186c8", "708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		rets = driver.listUsersInGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffd");
//		assertEquals(0, rets.size());
//	}
//
//	@Test
//	public void testDeleteUser() {
//		assertEquals(2, driver.listUsers().size());
//		driver.deleteUser("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
//		assertEquals(1, driver.listUsers().size());
//	}
//
//	@Test
//	public void testGetUserByName() {
//		User ret = driver.getUserByName("admin", "default");
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
//	public void testCreateGroup() {
//		Group group = new Group();
//		group.setName("new group");
//		group.setDescription("my group");
//		Domain domain = new Domain();
//		domain.setId("default");
//		group.setDomain(domain);
//		Group ret = driver.createGroup(group);
//		assertNotNull(ret.getId());
//		assertEquals(group.getName(), ret.getName());
//		assertEquals(group.getDescription(), ret.getDescription());
//		assertEquals(group.getDomainid(), ret.getDomainid());
//	}
//
//	@Test
//	public void testListGroups() {
//		List<Group> rets = driver.listGroups();
//		assertEquals(2, rets.size());
//	}
//
//	@Test
//	public void testListGroupsForUser() {
//		List<Group> rets = driver.listGroupsForUser("0f3328f8-a7e7-41b4-830d-be8fdd5186c8");
//		assertEquals(1, rets.size());
//		Group ret = rets.get(0);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffd", ret.getId());
//		assertEquals("demo_group", ret.getName());
//		assertEquals("demo group", ret.getDescription());
//		assertEquals("default", ret.getDomainid());
//	}
//
//	@Test
//	public void testGetGroup() {
//		Group ret = driver.getGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffc", ret.getId());
//		assertEquals("admin_group", ret.getName());
//		assertEquals("admin group", ret.getDescription());
//		assertEquals("default", ret.getDomainid());
//	}
//
//	@Test
//	public void testUpdateGroup() {
//		Group group = new Group();
//		group.setName("new group");
//		group.setDescription("my group");
//		Group ret = driver.updateGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffc", group);
//		assertEquals("708bb4f9-9d3c-46af-b18c-7033dc022ffc", ret.getId());
//		assertEquals(group.getName(), ret.getName());
//		assertEquals(group.getDescription(), ret.getDescription());
//		assertEquals("default", ret.getDomainid());
//	}
//
//	@Test
//	public void testDeleteGroup() {
//		assertEquals(2, driver.listGroups().size());
//		driver.deleteGroup("708bb4f9-9d3c-46af-b18c-7033dc022ffc");
//		assertEquals(1, driver.listGroups().size());
//	}
//
// }
