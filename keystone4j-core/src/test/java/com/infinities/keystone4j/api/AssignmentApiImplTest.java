package com.infinities.keystone4j.api;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;

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

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.AssignmentApiImpl;
import com.infinities.keystone4j.assignment.model.Assignment;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.GroupDomainGrant;
import com.infinities.keystone4j.assignment.model.GroupProjectGrant;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.assignment.model.UserDomainGrant;
import com.infinities.keystone4j.assignment.model.UserProjectGrant;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

public class AssignmentApiImplTest {

	private Mockery context;
	private AssignmentApi assignmentApi;
	private CredentialApi credentialApi;
	private IdentityApi identityApi;
	private TokenApi tokenApi;
	private AssignmentDriver driver;
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
		tokenApi = context.mock(TokenApi.class);
		credentialApi = context.mock(CredentialApi.class);
		identityApi = context.mock(IdentityApi.class);
		driver = context.mock(AssignmentDriver.class);

		domain = new Domain();
		domain.setDescription("desc of Domain");
		domain.setName("my domain");

		project = new Project();
		project.setDescription("desc of Project");
		project.setDomain(domain);
		project.setName("my project");

		user = new User();
		user.setId("newuser");
		user.setDescription("my user");
		user.setName("example user");
		user.setDefault_project(project);
		user.setDomain(domain);

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

		assignmentApi = new AssignmentApiImpl(credentialApi, identityApi, tokenApi, driver);
	}

	@After
	public void tearDown() throws Exception {
		context.assertIsSatisfied();
	}

	@Test
	public void testCreateDomain() {
		final String id = "newdomain";
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createDomain(domain);
				will(new CustomAction("add id to domain") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Domain domain = (Domain) invocation.getParameter(0);
						domain.setId(id);
						return domain;
					}

				});
			}
		});
		Domain ret = assignmentApi.createDomain(domain);
		assertEquals(domain.getName(), ret.getName());
		assertEquals(domain.getId(), ret.getId());
		assertEquals(domain.getDescription(), ret.getDescription());
	}

	@Test
	public void testListDomains() {
		final List<Domain> domains = new ArrayList<Domain>();
		domain.setId("domain1");
		domains.add(domain);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listDomains();
				will(returnValue(domains));
			}
		});
		List<Domain> ret = assignmentApi.listDomains();
		assertEquals(1, ret.size());
		Domain retDomain = ret.get(0);
		assertEquals(domain.getId(), retDomain.getId());
		assertEquals(domain.getName(), retDomain.getName());
		assertEquals(domain.getDescription(), retDomain.getDescription());

	}

	@Test
	public void testGetDomain() {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getDomain(domain.getId());
				will(returnValue(domain));
			}
		});
		Domain retDomain = assignmentApi.getDomain(domain.getId());
		assertEquals(domain.getId(), retDomain.getId());
		assertEquals(domain.getName(), retDomain.getName());
		assertEquals(domain.getDescription(), retDomain.getDescription());

	}

	@Test
	public void testUpdateDomain() {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).updateDomain(domain.getId(), domain);
				will(returnValue(domain));
			}
		});
		Domain retDomain = assignmentApi.updateDomain(domain.getId(), domain);
		assertEquals(domain.getId(), retDomain.getId());
		assertEquals(domain.getName(), retDomain.getName());
		assertEquals(domain.getDescription(), retDomain.getDescription());
	}

	@Test(expected = WebApplicationException.class)
	public void testDeleteDomainWithDefaultDomainId() {
		String defaultDomainid = Config.Instance.getOpt(Config.Type.identity, "default_domain_id").asText();
		domain.setId(defaultDomainid);
		assignmentApi.deleteDomain(domain.getId());
	}

	@Test(expected = WebApplicationException.class)
	public void testDeleteDomainWithEnable() {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getDomain(domain.getId());
				will(returnValue(domain));
			}
		});
		assignmentApi.deleteDomain(domain.getId());
	}

	@Test
	public void testDeleteDomain() {
		domain.setId("domain1");
		domain.setEnabled(false);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getDomain(domain.getId());
				will(returnValue(domain));
				exactly(1).of(driver).deleteDomain(domain.getId());
			}
		});
		assignmentApi.deleteDomain(domain.getId());
	}

	@Test
	public void testCreateProject() {
		final String id = "newproject";
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createProject(project);
				will(new CustomAction("add id to project") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Project project = (Project) invocation.getParameter(0);
						project.setId(id);
						return project;
					}

				});
			}
		});
		Project ret = assignmentApi.createProject(project);
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());
	}

	@Test
	public void testGetProject() {
		project.setId("project1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getProject(project.getId());
				will(returnValue(project));
			}
		});
		Project ret = assignmentApi.getProject(project.getId());
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());

	}

	@Test
	public void testUpdateProject() {
		project.setId("project1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).updateProject(project.getId(), project);
				will(returnValue(project));
			}
		});
		Project ret = assignmentApi.updateProject(project.getId(), project);
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());
	}

	@Test
	public void testDeleteProject() {
		project.setId("project1");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listUsersForProject(project.getId());
				will(returnValue(users));

				exactly(1).of(driver).getProject(project.getId());
				will(returnValue(project));

				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), project.getId());
				exactly(1).of(credentialApi).deleteCredentialsForProject(project.getId());
				exactly(1).of(driver).deleteProject(project.getId());
			}
		});
		assignmentApi.deleteProject(project.getId());
	}

	@Test
	public void testListProjects() {
		domain.setId("domain1");
		project.setId("project1");
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listProjects(null);
				will(returnValue(projects));
			}
		});

		List<Project> rets = assignmentApi.listProjects();
		assertEquals(1, rets.size());
		Project ret = rets.get(0);
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());

	}

	@Test
	public void testListProjectsForUser() {
		domain.setId("domain1");
		project.setId("project1");
		user.setId("user1");
		group.setId("group1");
		final List<Project> projects = new ArrayList<Project>();
		projects.add(project);
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);
		final List<String> groupids = Lists.newArrayList();
		groupids.add(group.getId());

		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).listGroupsForUser(user.getId(), null);
				will(returnValue(groups));
				exactly(1).of(driver).listProjectsForUser(user.getId(), groupids);
				will(returnValue(projects));
			}
		});

		List<Project> rets = assignmentApi.listProjectsForUser(user.getId());
		assertEquals(1, rets.size());
		Project ret = rets.get(0);
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());

	}

	@Test
	public void testCreateRole() {
		final String id = "newrole";
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createRole(role);
				will(new CustomAction("add id to role") {

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						Role role = (Role) invocation.getParameter(0);
						role.setId(id);
						return role;
					}

				});
			}
		});
		Role ret = assignmentApi.createRole(role);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testListRoles() {
		role.setId("role1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listRoles();
				will(returnValue(roles));
			}
		});

		List<Role> rets = assignmentApi.listRoles();
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());

	}

	@Test
	public void testGetRole() {
		role.setId("role1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getRole(role.getId());
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.getRole(role.getId());
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testUpdateRole() {
		role.setId("role1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).updateRole(role.getId(), role);
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.updateRole(role.getId(), role);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testDeleteRole() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		Assignment assignment = new Assignment();
		assignment.setId("newassign");
		assignment.setRole(role);
		assignment.setProject(project);
		assignment.setUser(user);
		final List<Assignment> assignments = new ArrayList<Assignment>();
		assignments.add(assignment);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listRoleAssignments();
				will(returnValue(assignments));
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), project.getId());
				exactly(1).of(driver).deleteRole(role.getId());
			}
		});
		assignmentApi.deleteRole(role.getId());
	}

	@Test
	public void testGetRolesForUserAndProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		final UserProjectGrant userProjectGrant = new UserProjectGrant();
		userProjectGrant.setId("userProjectGrant1");
		userProjectGrant.setProject(project);
		userProjectGrant.setUser(user);
		// UserProjectGrantMetadata userProjectGrantMetadata = new
		// UserProjectGrantMetadata();
		// userProjectGrantMetadata.setId("userProjectGrantMetadata1");
		userProjectGrant.setRole(role);
		// userProjectGrant.getMetadatas().add(userProjectGrantMetadata);

		final GroupProjectGrant groupProjectGrant = new GroupProjectGrant();
		groupProjectGrant.setId("groupProjectGrant");
		groupProjectGrant.setProject(project);
		groupProjectGrant.setGroup(group);
		// GroupProjectGrantMetadata groupProjectGrantMetadata = new
		// GroupProjectGrantMetadata();
		// groupProjectGrantMetadata.setId("groupProjectGrantMetadata1");
		groupProjectGrant.setRole(role);
		// groupProjectGrant.getMetadatas().add(groupProjectGrantMetadata);
		final List<UserProjectGrant> userProjectGrants = new ArrayList<UserProjectGrant>();
		userProjectGrants.add(userProjectGrant);
		final List<GroupProjectGrant> groupProjectGrants = new ArrayList<GroupProjectGrant>();
		groupProjectGrants.add(groupProjectGrant);
		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getProject(project.getId());
				will(returnValue(project));
				exactly(1).of(driver).getUserProjectGrants(user.getId(), project.getId());
				will(returnValue(userProjectGrants));
				exactly(1).of(identityApi).listGroupsForUser(user.getId(), null);
				will(returnValue(groups));
				exactly(1).of(driver).getGroupProjectGrants(group.getId(), project.getId());
				will(returnValue(groupProjectGrants));
			}
		});
		List<Role> rets = assignmentApi.getRolesForUserAndProject(user.getId(), project.getId());
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testCreateGrantByUserDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		assignmentApi.createGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testCreateGrantByGroupDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createGrantByGroupDomain(role.getId(), group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		assignmentApi.createGrantByGroupDomain(role.getId(), group.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testCreateGrantByGroupProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createGrantByGroupProject(role.getId(), user.getId(), project.getId());
			}
		});
		assignmentApi.createGrantByGroupProject(role.getId(), user.getId(), project.getId());
	}

	@Test
	public void testCreateGrantByUserProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).createGrantByUserProject(role.getId(), user.getId(), project.getId());
			}
		});
		assignmentApi.createGrantByUserProject(role.getId(), user.getId(), project.getId());
	}

	@Test
	public void testListGrantsByUserDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listGrantsByUserDomain(user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		List<Role> rets = assignmentApi.listGrantsByUserDomain(user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testListGrantsByGroupDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listGrantsByGroupDomain(group.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		List<Role> rets = assignmentApi.listGrantsByGroupDomain(group.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testListGrantsByGroupProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listGrantsByGroupProject(user.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		List<Role> rets = assignmentApi.listGrantsByGroupProject(user.getId(), project.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testListGrantsByUserProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listGrantsByUserProject(user.getId(), project.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(roles));
			}
		});
		List<Role> rets = assignmentApi.listGrantsByUserProject(user.getId(), project.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(1, rets.size());
		Role ret = rets.get(0);
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testGetGrantByUserDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.getGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testGetGrantByGroupDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getGrantByGroupDomain(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.getGrantByGroupDomain(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testGetGrantByGroupProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getGrantByGroupProject(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.getGrantByGroupProject(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testGetGrantByUserProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getGrantByUserProject(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
				will(returnValue(role));
			}
		});
		Role ret = assignmentApi.getGrantByUserProject(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
		assertEquals(role.getName(), ret.getName());
		assertEquals(role.getId(), ret.getId());
		assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testDeleteGrantByUserDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
				exactly(1).of(driver).deleteGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		assignmentApi.deleteGrantByUserDomain(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testDeleteGrantByGroupDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		final List<User> users = new ArrayList<User>();
		users.add(user);

		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).listUsersInGroup(user.getId(), null);
				will(returnValue(users));

				exactly(1).of(driver).deleteGrantByGroupDomain(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());

				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
			}
		});
		assignmentApi.deleteGrantByGroupDomain(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testDeleteGrantByGroupProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		final List<User> users = new ArrayList<User>();
		users.add(user);

		context.checking(new Expectations() {

			{
				exactly(1).of(identityApi).listUsersInGroup(user.getId(), null);
				will(returnValue(users));
				exactly(1).of(driver).deleteGrantByGroupProject(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());

				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
			}
		});
		assignmentApi.deleteGrantByGroupProject(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testDeleteGrantByUserProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");

		context.checking(new Expectations() {

			{
				exactly(1).of(tokenApi).deleteTokensForUser(user.getId(), null);
				exactly(1).of(driver).deleteGrantByUserProject(role.getId(), user.getId(), domain.getId(),
						Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
			}
		});
		assignmentApi.deleteGrantByUserProject(role.getId(), user.getId(), domain.getId(),
				Config.Instance.getOpt(Config.Type.os_inherit, "enabled").asBoolean());
	}

	@Test
	public void testListRoleAssignmentsForRole() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		final List<User> users = new ArrayList<User>();
		users.add(user);
		Assignment assignment = new Assignment();
		assignment.setId("newassign");
		assignment.setRole(role);
		assignment.setProject(project);
		assignment.setUser(user);
		final List<Assignment> assignments = new ArrayList<Assignment>();
		assignments.add(assignment);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listRoleAssignments();
				will(returnValue(assignments));
			}
		});

		List<Assignment> rets = assignmentApi.listRoleAssignmentsForRole(role.getId());
		assertEquals(1, rets.size());
		Assignment ret = rets.get(0);
		assertEquals(assignment.getId(), ret.getId());
		assertEquals(assignment.getRole(), ret.getRole());
		assertEquals(assignment.getProject(), ret.getProject());
		assertEquals(assignment.getUser(), ret.getUser());

	}

	@Test
	public void testListUsersForProject() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		final List<User> users = new ArrayList<User>();
		users.add(user);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).listUsersForProject(project.getId());
				will(returnValue(users));
			}
		});

		List<User> rets = assignmentApi.listUsersForProject(project.getId());
		assertEquals(1, rets.size());
		User ret = rets.get(0);
		assertEquals(user.getId(), ret.getId());
		assertEquals(user.getDescription(), ret.getDescription());
		assertEquals(user.getName(), ret.getName());
		assertEquals(user.getDomain(), ret.getDomain());
		assertEquals(user.getDefault_project(), ret.getDefault_project());

	}

	@Test
	public void testGetRolesForUserAndDomain() {
		role.setId("role1");
		user.setId("user1");
		project.setId("project1");
		group.setId("group1");
		domain.setId("domain1");
		final List<Role> roles = new ArrayList<Role>();
		roles.add(role);

		final UserDomainGrant grant = new UserDomainGrant();
		grant.setId("grant1");
		grant.setDomain(domain);
		grant.setUser(user);
		// UserDomainGrantMetadata metadata = new UserDomainGrantMetadata();
		// metadata.setId("metadata1");
		// metadata.setGrant(grant);
		grant.setRole(role);
		// grant.getMetadatas().add(metadata);

		final List<Group> groups = new ArrayList<Group>();
		groups.add(group);

		final GroupDomainGrant groupDomainGrant = new GroupDomainGrant();
		groupDomainGrant.setId("grant2");
		groupDomainGrant.setGroup(group);
		groupDomainGrant.setDomain(domain);
		// GroupDomainGrantMetadata groupMetadata = new
		// GroupDomainGrantMetadata();
		// groupMetadata.setId("metadata2");
		// groupMetadata.setGrant(groupDomainGrant);
		// groupMetadata.setRole(role);
		// groupDomainGrant.getGroupDomainGrantMetadatas().add(groupMetadata);
		final List<UserDomainGrant> userDomainGrants = new ArrayList<UserDomainGrant>();
		userDomainGrants.add(grant);

		final List<GroupDomainGrant> groupDomainGrants = new ArrayList<GroupDomainGrant>();
		groupDomainGrants.add(groupDomainGrant);

		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getDomain(domain.getId());
				will(returnValue(domain));

				exactly(1).of(driver).getGroupDomainGrants(group.getId(), domain.getId());
				will(returnValue(groupDomainGrants));

				exactly(1).of(driver).getUserDomainGrants(user.getId(), domain.getId());
				will(returnValue(userDomainGrants));

				exactly(1).of(identityApi).listGroupsForUser(user.getId(), null);
				will(returnValue(groups));

			}
		});
		List<Role> rets = assignmentApi.getRolesForUserAndDomain(user.getId(), domain.getId());
		assertEquals(2, rets.size());
		// Role ret = rets.get(0);
		// assertEquals(role.getName(), ret.getName());
		// assertEquals(role.getId(), ret.getId());
		// assertEquals(role.getDescription(), ret.getDescription());
	}

	@Test
	public void testGetDomainByName() {
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getDomainByName(domain.getName());
				will(returnValue(domain));
			}
		});
		Domain retDomain = assignmentApi.getDomainByName(domain.getName());
		assertEquals(domain.getId(), retDomain.getId());
		assertEquals(domain.getName(), retDomain.getName());
		assertEquals(domain.getDescription(), retDomain.getDescription());

	}

	@Test
	public void testGetProjectByName() {
		project.setId("project1");
		domain.setId("domain1");
		context.checking(new Expectations() {

			{
				exactly(1).of(driver).getProjectByName(project.getName(), domain.getId());
				will(returnValue(project));
			}
		});
		Project ret = assignmentApi.getProjectByName(project.getName(), domain.getId());
		assertEquals(project.getName(), ret.getName());
		assertEquals(project.getId(), ret.getId());
		assertEquals(project.getDescription(), ret.getDescription());
		assertEquals(project.getDomain(), ret.getDomain());

	}

}
