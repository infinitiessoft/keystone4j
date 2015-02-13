//package com.infinities.keystone4j.api;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.jmock.Expectations;
//import org.jmock.Mockery;
//import org.jmock.api.Invocation;
//import org.jmock.integration.junit4.JUnit4Mockery;
//import org.jmock.lib.action.CustomAction;
//import org.jmock.lib.concurrent.Synchroniser;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.infinities.keystone4j.catalog.CatalogApi;
//import com.infinities.keystone4j.catalog.CatalogDriver;
//import com.infinities.keystone4j.catalog.api.CatalogApiImpl;
//import com.infinities.keystone4j.model.assignment.Domain;
//import com.infinities.keystone4j.model.assignment.Project;
//import com.infinities.keystone4j.model.assignment.Role;
//import com.infinities.keystone4j.model.catalog.Catalog;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//import com.infinities.keystone4j.model.catalog.Service;
//import com.infinities.keystone4j.model.identity.Group;
//import com.infinities.keystone4j.model.identity.User;
//import com.infinities.keystone4j.model.token.Token;
//
//public class CatalogApiImplTest {
//
//	private Mockery context;
//	private CatalogApi catalogApi;
//	private CatalogDriver driver;
//	private Endpoint endpoint;
//	private Service service;
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
//		context = new JUnit4Mockery() {
//
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//
//		driver = context.mock(CatalogDriver.class);
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
//
//		service = new Service();
//		service.setDescription("Keystone Identity Service");
//		service.setName("keystone");
//		service.setType("identity");
//		service.setId("newserviceid");
//
//		endpoint = new Endpoint();
//		endpoint.setInterfaceType("internal");
//		endpoint.setName("the internal volume endpoint");
//		endpoint.setUrl("http://identity:35357/v3/endpoints/");
//		endpoint.setService(service);
//		catalogApi = new CatalogApiImpl(driver);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testCreateEndpoint() {
//		final String id = "newendpoint";
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).createEndpoint(endpoint);
//				will(new CustomAction("add id to endpoint") {
//
//					@Override
//					public Object invoke(Invocation invocation) throws Throwable {
//						Endpoint endpoint = (Endpoint) invocation.getParameter(0);
//						endpoint.setId(id);
//						return endpoint;
//					}
//
//				});
//			}
//		});
//		Endpoint ret = catalogApi.createEndpoint(endpoint);
//		assertEquals(id, ret.getId());
//		assertEquals(endpoint.getName(), ret.getName());
//		assertEquals(endpoint.getInterfaceType(), ret.getInterfaceType());
//		assertEquals(endpoint.getService(), ret.getService());
//		assertEquals(endpoint.getUrl(), ret.getUrl());
//	}
//
//	@Test
//	public void testListEndpoints() {
//		endpoint.setId("newendpoint");
//		final List<Endpoint> endpoints = new ArrayList<Endpoint>();
//		endpoints.add(endpoint);
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).listEndpoints();
//				will(returnValue(endpoints));
//			}
//		});
//		List<Endpoint> rets = catalogApi.listEndpoints();
//		assertEquals(1, rets.size());
//		Endpoint ret = rets.get(0);
//		assertEquals(endpoint.getId(), ret.getId());
//		assertEquals(endpoint.getName(), ret.getName());
//		assertEquals(endpoint.getInterfaceType(), ret.getInterfaceType());
//		assertEquals(endpoint.getService(), ret.getService());
//		assertEquals(endpoint.getUrl(), ret.getUrl());
//	}
//
//	@Test
//	public void testGetEndpoint() {
//		endpoint.setId("newendpoint");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).getEndpoint(endpoint.getId());
//				will(returnValue(endpoint));
//			}
//		});
//		Endpoint ret = catalogApi.getEndpoint(endpoint.getId());
//		assertEquals(endpoint.getId(), ret.getId());
//		assertEquals(endpoint.getName(), ret.getName());
//		assertEquals(endpoint.getInterfaceType(), ret.getInterfaceType());
//		assertEquals(endpoint.getService(), ret.getService());
//		assertEquals(endpoint.getUrl(), ret.getUrl());
//	}
//
//	@Test
//	public void testUpdateEndpoint() {
//		endpoint.setId("newendpoint");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).updateEndpoint(endpoint.getId(), endpoint);
//				will(returnValue(endpoint));
//			}
//		});
//		Endpoint ret = catalogApi.updateEndpoint(endpoint.getId(), endpoint);
//		assertEquals(endpoint.getId(), ret.getId());
//		assertEquals(endpoint.getName(), ret.getName());
//		assertEquals(endpoint.getInterfaceType(), ret.getInterfaceType());
//		assertEquals(endpoint.getService(), ret.getService());
//		assertEquals(endpoint.getUrl(), ret.getUrl());
//	}
//
//	@Test
//	public void testDeleteEndpoint() {
//		endpoint.setId("newendpoint");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).deleteEndpoint(endpoint.getId());
//			}
//		});
//		catalogApi.deleteEndpoint(endpoint.getId());
//	}
//
//	@Test
//	public void testCreateService() {
//		final String id = "newservice";
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).createService(service);
//				will(new CustomAction("add id to service") {
//
//					@Override
//					public Object invoke(Invocation invocation) throws Throwable {
//						Service service = (Service) invocation.getParameter(0);
//						service.setId(id);
//						return service;
//					}
//
//				});
//			}
//		});
//		Service ret = catalogApi.createService(service);
//		assertEquals(id, ret.getId());
//		assertEquals(service.getName(), ret.getName());
//		assertEquals(service.getDescription(), ret.getDescription());
//		assertEquals(service.getType(), ret.getType());
//	}
//
//	@Test
//	public void testListServices() {
//		service.setId("newservice");
//		final List<Service> services = new ArrayList<Service>();
//		services.add(service);
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).listServices();
//				will(returnValue(services));
//			}
//		});
//		List<Service> rets = catalogApi.listServices();
//		assertEquals(1, rets.size());
//		Service ret = rets.get(0);
//		assertEquals(service.getId(), ret.getId());
//		assertEquals(service.getName(), ret.getName());
//		assertEquals(service.getDescription(), ret.getDescription());
//		assertEquals(service.getType(), ret.getType());
//	}
//
//	@Test
//	public void testGetService() {
//		service.setId("newservice");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).getService(service.getId());
//				will(returnValue(service));
//			}
//		});
//		Service ret = catalogApi.getService(service.getId());
//		assertEquals(service.getId(), ret.getId());
//		assertEquals(service.getName(), ret.getName());
//		assertEquals(service.getDescription(), ret.getDescription());
//		assertEquals(service.getType(), ret.getType());
//	}
//
//	@Test
//	public void testUpdateService() {
//		service.setId("newservice");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).updateService(service.getId(), service);
//				will(returnValue(service));
//			}
//		});
//		Service ret = catalogApi.updateService(service.getId(), service);
//		assertEquals(service.getId(), ret.getId());
//		assertEquals(service.getName(), ret.getName());
//		assertEquals(service.getDescription(), ret.getDescription());
//		assertEquals(service.getType(), ret.getType());
//	}
//
//	@Test
//	public void testDeleteService() {
//		service.setId("newservice");
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).deleteService(service.getId());
//			}
//		});
//		catalogApi.deleteService(service.getId());
//	}
//
//	@Test
//	public void testGetV3Catalog() {
//		service.setId("newservice");
//		final List<Service> services = new ArrayList<Service>();
//		services.add(service);
//		context.checking(new Expectations() {
//
//			{
//				exactly(1).of(driver).listServices();
//				will(returnValue(services));
//			}
//		});
//		Catalog catalog = catalogApi.getV3Catalog(user.getId(), project.getId());
//		List<Service> rets = catalog.getServices();
//		assertEquals(1, rets.size());
//		Service ret = rets.get(0);
//		assertEquals(service.getId(), ret.getId());
//		assertEquals(service.getName(), ret.getName());
//		assertEquals(service.getDescription(), ret.getDescription());
//		assertEquals(service.getType(), ret.getType());
//	}
// }
