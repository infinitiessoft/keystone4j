//package com.infinities.keystone4j.driver;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.List;
//
//import org.jmock.Mockery;
//import org.jmock.integration.junit4.JUnit4Mockery;
//import org.jmock.lib.concurrent.Synchroniser;
//import org.jmock.lib.legacy.ClassImposteriser;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.infinities.keystone4j.catalog.CatalogDriver;
//import com.infinities.keystone4j.catalog.driver.CatalogJpaDriver;
//import com.infinities.keystone4j.model.catalog.Endpoint;
//import com.infinities.keystone4j.model.catalog.Service;
//
//public class CatalogJpaDriverTest extends AbstractDbUnitJpaTest {
//
//	private Mockery context;
//	private CatalogDriver driver;
//
//
//	@Before
//	public void setUp() throws Exception {
//		driver = new CatalogJpaDriver();
//
//		context = new JUnit4Mockery() {
//
//			{
//				setImposteriser(ClassImposteriser.INSTANCE);
//				setThreadingPolicy(new Synchroniser());
//			}
//		};
//
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		context.assertIsSatisfied();
//	}
//
//	@Test
//	public void testCreateService() {
//		Service input = new Service();
//		input.setDescription("new service");
//		input.setName("my service");
//		input.setType("identity");
//		Service ret = driver.createService(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getType(), ret.getType());
//	}
//
//	@Test
//	public void testListServices() {
//		List<Service> rets = driver.listServices();
//		assertEquals(1, rets.size());
//		Service ret = rets.get(0);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getId());
//		assertEquals("keystone", ret.getName());
//		assertEquals("identity service", ret.getDescription());
//		assertEquals("identity", ret.getType());
//	}
//
//	@Test
//	public void testGetService() {
//		Service ret = driver.getService("79ea2c65-4679-441f-a596-8aec16752a0f");
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getId());
//		assertEquals("keystone", ret.getName());
//		assertEquals("identity service", ret.getDescription());
//		assertEquals("identity", ret.getType());
//	}
//
//	@Test
//	public void testUpdateService() {
//		Service input = new Service();
//		input.setDescription("new service");
//		input.setName("my service");
//		input.setType("identity");
//		Service ret = driver.updateService("79ea2c65-4679-441f-a596-8aec16752a0f", input);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getType(), ret.getType());
//	}
//
//	@Test
//	public void testDeleteService() {
//		List<Service> rets = driver.listServices();
//		assertEquals(1, rets.size());
//		driver.deleteService("79ea2c65-4679-441f-a596-8aec16752a0f");
//		rets = driver.listServices();
//		assertEquals(0, rets.size());
//	}
//
//	@Test
//	public void testCreateEndpoint() {
//		Endpoint input = new Endpoint();
//		input.setDescription("keystone endpoint");
//		input.setName("keystone_endpoint");
//		input.setInterfaceType("public");
//		Service service = new Service();
//		service.setId("79ea2c65-4679-441f-a596-8aec16752a0f");
//		input.setService(service);
//		input.setUrl("http://localhost:%(public_port)/");
//		Endpoint ret = driver.createEndpoint(input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getUrl(), ret.getUrl());
//		assertEquals(input.getInterfaceType(), ret.getInterfaceType());
//		assertEquals(input.getServiceid(), ret.getServiceid());
//	}
//
//	@Test
//	public void testGetEndpoint() {
//		Endpoint ret = driver.getEndpoint("79ea2c65-4679-441f-a596-8aec16752a1f");
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a1f", ret.getId());
//		assertEquals("keystone_endpoint", ret.getName());
//		assertEquals("keystone endpoint", ret.getDescription());
//		assertEquals("http://localhost:%(public_port)/", ret.getUrl());
//		assertEquals("public", ret.getInterfaceType());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getServiceid());
//	}
//
//	@Test
//	public void testUpdateEndpoint() {
//		Endpoint input = new Endpoint();
//		input.setDescription("demo endpoint");
//		input.setName("demo_endpoint");
//		input.setInterfaceType("public");
//		input.setUrl("http://localhost:%(public_port)/");
//
//		Endpoint ret = driver.updateEndpoint("79ea2c65-4679-441f-a596-8aec16752a1f", input);
//		assertNotNull(ret.getId());
//		assertEquals(input.getName(), ret.getName());
//		assertEquals(input.getDescription(), ret.getDescription());
//		assertEquals(input.getUrl(), ret.getUrl());
//		assertEquals(input.getInterfaceType(), ret.getInterfaceType());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getServiceid());
//	}
//
//	@Test
//	public void testDeleteEndpoint() {
//		List<Endpoint> rets = driver.listEndpoints();
//		assertEquals(1, rets.size());
//		driver.deleteEndpoint("79ea2c65-4679-441f-a596-8aec16752a1f");
//		rets = driver.listEndpoints();
//		assertEquals(0, rets.size());
//	}
//
//	@Test
//	public void testListEndpoints() {
//		List<Endpoint> rets = driver.listEndpoints();
//		assertEquals(1, rets.size());
//		Endpoint ret = rets.get(0);
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a1f", ret.getId());
//		assertEquals("keystone_endpoint", ret.getName());
//		assertEquals("keystone endpoint", ret.getDescription());
//		assertEquals("http://localhost:%(public_port)/", ret.getUrl());
//		assertEquals("public", ret.getInterfaceType());
//		assertEquals("79ea2c65-4679-441f-a596-8aec16752a0f", ret.getServiceid());
//	}
//
// }
