package com.infinities.keystone4j.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.IdentityApiImpl;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.TokenApi;

public class IdentityApiImplTest {

	private Mockery context;
	// private AssignmentApi assignmentApi;
	private CredentialApi credentialApi;
	private IdentityApi identityApi;
	private TokenApi tokenApi;
	private IdentityDriver driver;
	private Endpoint endpoint;
	private Service service;
	private Domain domain;
	private Project project;
	private User user;
	private Token token;
	private Group group;
	private Role role;


	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery() {

			{
				setImposteriser(ClassImposteriser.INSTANCE);
				setThreadingPolicy(new Synchroniser());
			}
		};

		// assignmentApi = context.mock(AssignmentApi.class);
		tokenApi = context.mock(TokenApi.class);
		credentialApi = context.mock(CredentialApi.class);
		driver = context.mock(IdentityDriver.class);

		domain = new Domain();
		domain.setDescription("desc of Domain");
		domain.setName("my domain");

		project = new Project();
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		// user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);
		user.setPassword("password");

		token = new Token();
		token.setProject(project);
		token.setExpires(new Date());
		token.setId("newtoken");
		token.setIssueAt(new Date());
		token.setUser(user);
		user.getTokens().add(token);

		group = new Group();
		group.setDescription("my group");
		group.setDomain(domain);
		group.setName("newgroup");

		role = new Role();
		role.setDescription("my role1");
		role.setName("example role1");

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
		identityApi = new IdentityApiImpl(credentialApi, tokenApi, driver);
	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	@Test
	public void testGetUser() {
		user.setId("newuser");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getUser(user.getId());
				will(returnValue(user));

				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		User ret = identityApi.getUser(user.getId(), domain.getId());
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
	}

	@Test
	public void testGetGroup() {
		group.setId("newgroup");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getGroup(group.getId());
				will(returnValue(group));

				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		Group ret = identityApi.getGroup(group.getId(), domain.getId());
		assertEquals(group.getId(), ret.getId());
		assertEquals(group.getName(), ret.getName());
		assertEquals(group.getDescription(), ret.getDescription());
		assertEquals(group.getDomain(), ret.getDomain());
	}

	@Test
	public void testCreateGroup() {
		final String id = "newgroup";
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createGroup(group);
				will(new CustomAction("add id to group") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Group group = (Group) invocation.getParameter(0);
						group.setId(id);
						return group;
					}

				});

				exactly(2).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		Group ret = identityApi.createGroup(group);
		assertEquals(id, ret.getId());
		assertEquals(group.getId(), ret.getId());
		assertEquals(group.getName(), ret.getName());
		assertEquals(group.getDescription(), ret.getDescription());
		assertEquals(group.getDomain(), ret.getDomain());
	}

	@Test
	public void testListGroups() {
		group.setId("newgroup");
		domain.setId("newdomain");
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).listGroups();
				will(returnValue(groups));
			}
		});
		List<Group> rets = identityApi.listGroups(domain.getId());
		assertEquals(1, rets.size());
		Group ret = rets.get(0);
		assertEquals(group.getId(), ret.getId());
		assertEquals(group.getName(), ret.getName());
		assertEquals(group.getDescription(), ret.getDescription());
		assertEquals(group.getDomain(), ret.getDomain());
	}

	@Test
	public void testUpdateGroup() {
		group.setId("newgroup");
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(2).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).updateGroup(group.getId(), group);
				will(returnValue(group));
			}
		});
		Group ret = identityApi.updateGroup(group.getId(), group, domain.getId());
		assertEquals(group.getId(), ret.getId());
		assertEquals(group.getName(), ret.getName());
		assertEquals(group.getDescription(), ret.getDescription());
		assertEquals(group.getDomain(), ret.getDomain());
	}

	@Test
	public void testDeleteGroup() {
		project.setId("newproject");
		group.setId("newgroup");
		domain.setId("newdomain");
		user.setId("newuser");
		final List<User> users = new ArrayList<User>();
		users.add(user);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listUsersInGroup(group.getId());
				will(returnValue(users));

				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);

				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).deleteGroup(group.getId());
			}
		});
		identityApi.deleteGroup(group.getId(), domain.getId());
	}

	@Test
	public void testListGroupsForUser() {
		group.setId("newgroup");
		domain.setId("newdomain");
		user.setId("newuser");
		project.setId("newproject");
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).listGroupsForUser(user.getId());
				will(returnValue(groups));
			}
		});
		List<Group> rets = identityApi.listGroupsForUser(user.getId(), domain.getId());
		assertEquals(1, rets.size());
		Group ret = rets.get(0);
		assertEquals(group.getId(), ret.getId());
		assertEquals(group.getName(), ret.getName());
		assertEquals(group.getDescription(), ret.getDescription());
		assertEquals(group.getDomain(), ret.getDomain());
	}

	@Test
	public void testCreateUser() {
		final String id = "newuser";
		domain.setId("newduser");
		project.setId("newproject");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createUser(user);
				will(new CustomAction("add id to user") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						User user = (User) invocation.getParameter(0);
						user.setId(id);
						return user;
					}

				});

				exactly(2).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		User ret = identityApi.createUser(user);
		assertEquals(id, ret.getId());
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
		assertEquals(user.getEmail(), user.getEmail());
	}

	@Test
	public void testListUsers() {
		group.setId("newgroup");
		domain.setId("newdomain");
		project.setId("newproject");
		user.setId("newuser");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).listUsers();
				will(returnValue(users));
			}
		});
		List<User> rets = identityApi.listUsers(domain.getId());
		assertEquals(1, rets.size());
		User ret = rets.get(0);
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
		assertEquals(user.getEmail(), user.getEmail());
	}

	@Test
	public void testUpdateUser() {
		user.setId("newuser");
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(2).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).updateUser(user.getId(), user);
				will(returnValue(user));
			}
		});
		User ret = identityApi.updateUser(user.getId(), user, domain.getId());
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
		assertEquals(user.getEmail(), user.getEmail());
	}

	@Test
	public void testDeleteUser() {
		user.setId("newuser");
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).deleteUser(user.getId());
				exactly(1).of(credentialApi).deleteCredentialsForUser(user.getId());
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
			}
		});
		identityApi.deleteUser(user.getId(), domain.getId());
	}

	@Test
	public void testListUsersInGroup() {
		group.setId("newgroup");
		domain.setId("newdomain");
		project.setId("newproject");
		user.setId("newuser");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));

				exactly(1).of(driver).listUsersInGroup(group.getId());
				will(returnValue(users));
			}
		});
		List<User> rets = identityApi.listUsersInGroup(group.getId(), domain.getId());
		assertEquals(1, rets.size());
		User ret = rets.get(0);
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
		assertEquals(user.getEmail(), user.getEmail());
	}

	@Test
	public void testRemoveUserFromGroup() {
		user.setId("newuser");
		domain.setId("newdomain");
		group.setId("newgroup");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).removeUserFromGroup(user.getId(), group.getId());
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
			}
		});
		identityApi.removeUserFromGroup(user.getId(), group.getId(), domain.getId());
	}

	@Test
	public void testCheckUserInGroup() {
		user.setId("newuser");
		domain.setId("newdomain");
		group.setId("newgroup");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).checkUserInGroup(user.getId(), group.getId());
			}
		});
		identityApi.checkUserInGroup(user.getId(), group.getId(), domain.getId());
	}

	@Test
	public void testAddUserToGroup() {
		user.setId("newuser");
		domain.setId("newdomain");
		group.setId("newgroup");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).addUserToGroup(user.getId(), group.getId());
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
			}
		});
		identityApi.addUserToGroup(user.getId(), group.getId(), domain.getId());
	}

	@Test
	public void testAuthenticate() {
		user.setId("newuser");
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).authenticate(user.getId(), user.getPassword());
				will(returnValue(user));

				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		User ret = identityApi.authenticate(user.getId(), user.getPassword(), user.getDomainid());
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
	}

	@Test
	public void testGetUserByName() {
		user.setId("newuser");
		domain.setId("newdomain");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getUserByName(user.getName(), domain.getId());
				will(returnValue(user));

				exactly(1).of(driver).isDomainAware();
				will(returnValue(true));
			}
		});
		User ret = identityApi.getUserByName(user.getName(), domain.getId());
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());
	}
}
