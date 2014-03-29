package com.infinities.keystone4j.policy;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.infinities.keystone4j.common.Config;

public class EnforcerTest {

	private final static String POLICY_FILE = "policy_file";


	// private Enforcer enforcer;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEnforcer() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		String policyPath = Config.Instance.getOpt(Config.Type.DEFAULT, POLICY_FILE).asText();
		new Enforcer(policyPath);
	}

	// @Test
	// public void testSetRules() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testEnforce() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetRules() {
	// fail("Not yet implemented");
	// }

}
